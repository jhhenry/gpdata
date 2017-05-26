package dailyRun;

import henry.commons.CalendarUtil;
import henry.html.extractor.DailyQuoteFromSohu;
import henry.html.extractor.IStringVisitor;
import henry.persistent.BasicDailyQuoteService;
import henry.persistent.EMProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DailyQuoteRetrieval implements IStringVisitor
{
    private static final Logger logger = LogManager.getLogger();
    private static Pattern p = Pattern
            .compile("\\[\"(\\d{4}-\\d{2}-\\d{2})\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.\\-]+)\",\"([0-9.%\\-]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.%]+)\"\\]");

    // private Date start;
    // private Date end;
    private String stock;
    private ExecutorService exe = Executors.newFixedThreadPool(10);

    public DailyQuoteRetrieval(String stock)// Date start, Date end,
    {
        super();
        this.stock = stock;
    }
    
    public void getAll(Date start, Date end, List<String> stocks)
    {
        List<Task> tasks = new ArrayList<Task>();
        for (String stock : stocks) {
            if (stock != null)
                tasks.add(new Task(stock, start, end));
        }

        try {
            exe.invokeAll(tasks);
            exe.shutdown();
        }
        catch (InterruptedException e) {
            logger.catching(e);
        }
    }

    public static class Task implements Callable<Void>
    {
        private String stock;
        private Date start;
        private Date end;

        public Task(String stock, Date start, Date end)
        {
            super();
            this.stock = stock;
            this.start = start;
            this.end = end;
        }

        @Override
        public Void call() throws Exception
        {
            DailyQuoteRetrieval d = new DailyQuoteRetrieval(stock);
            d.getQuota(start, end);
            return null;
        }

    }
    
    public void getQuota(Date start, Date end)
    {
        logger.printf(Level.TRACE, "getting stock %1$s from %2$tm %2$te,%2$tY to  %3$tm %3$te,%3$tY", stock, start, end);
        DailyQuoteFromSohu.getDaily(start, end, stock, this);
        logger.trace("finished stock {}", stock);
    }

    @Override
    public void onString(String content)
    {
        extractStatsFrom(content);

    }

    private void extractStatsFrom(String content)
    {
        EntityManager em = EMProvider.getEM();
        BasicDailyQuoteService svr = new BasicDailyQuoteService(em);
        em.getTransaction().begin();
        Matcher m = p.matcher(content);
        while (m.find()) {
            int c = m.groupCount();
            if (c < 10)
                continue;
            String dateStr = m.group(1);
            double open = Double.valueOf(m.group(2));
            double close = Double.valueOf(m.group(3));
            double gap = Double.valueOf(m.group(4));
            double lowest = Double.valueOf(m.group(6));
            double highest = Double.valueOf(m.group(7));
            long vol = Long.valueOf(m.group(8));
            double deal = Double.valueOf(m.group(9));
            String t = m.group(10);
            double turnover = Double.valueOf(t.substring(0, t.length() - 1));
            logger.trace("date {}, open {}, close {}, gap {}, lowest {}, highest {}, vol {}, deal {}, turnover {}", dateStr, open, close, gap, lowest, highest, vol, deal, turnover);
            try {
                Date d = CalendarUtil.getFormattedDate(dateStr, "-");
                svr.createRecord(d, stock, open, close, gap, lowest, highest, vol, deal, turnover);
            }
            catch (Exception e) {
                logger.throwing(e);
            }
        }
        em.getTransaction().commit();
        // em.close();
    }

}
