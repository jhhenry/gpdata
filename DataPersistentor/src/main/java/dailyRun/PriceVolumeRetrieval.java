package dailyRun;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import henry.commons.CalendarUtil;
import henry.commons.network.HTTPConnector;
import henry.html.extractor.HtmlExtractor;
import henry.persistent.integrator.SinaDayPriceVolCapturer;

public class PriceVolumeRetrieval
{
	private static final Logger logger = LogManager.getLogger();
    
    private static final String PRICE_VOLUME_FROM_SINA = "http://market.finance.sina.com.cn/pricehis.php?symbol=%s&startdate=%s&enddate=%s";
	// private static final Logger logger =
    // LogManager.getLogger("AfterDayRetrieval");
    
    private ExecutorService exe = Executors.newFixedThreadPool(10);
    

    public List<Future<Object>> capture(Calendar start, Calendar end, final List<String> stocks)
    {
        List<Future<Object>> fs = new ArrayList<Future<Object>>();
        if (stocks == null)
            return fs;
        for (final String s : stocks) {
            fs.addAll(capture(start, end, s));
        }
        try {
            exe.shutdown();
            for (int i = 0; i < 15; i++) {
                if (exe.isTerminated()) {
                    break;
                }
                exe.awaitTermination(60, TimeUnit.SECONDS);
                logger.warn("{} minute has passed. waiting for AfterDayRetrieval finish", (i + 1));
            }
        }
        catch (InterruptedException e) {
            logger.error("error happened during the AfterDayRetrieval's exe waiting for termination", e);
        }
        return fs;
    }

    public List<Future<Object>> capture(Calendar start, Calendar end, final String stock)
    {
        List<Future<Object>> fs = new ArrayList<Future<Object>>();
        Calendar c = (Calendar) start.clone();

        while (c.compareTo(end) <= 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            final Date day = c.getTime();
            final String dateStr = sdf.format(day);

            Future<Object> f = exe.submit(new Callable<Object>()
            {
                @Override
                public Object call()
                {

                    String url = String.format(PRICE_VOLUME_FROM_SINA, stock, dateStr, dateStr);
                    logger.info("getting stock {} on {}", stock, dateStr);
                    boolean pass = false;
                    while (!pass) {
                        pass = run(stock, day, url);
                    }
                    return null;
                }

                private boolean run(final String stock, final Date day, String url)
                {
                    boolean pass = false;
                    try (InputStream is = new HTTPConnector(url).connect()) {
                        HtmlExtractor ext = new HtmlExtractor();
                        ext.extract(is, new SinaDayPriceVolCapturer(day, stock));
                        pass = true;
                    }
                    catch (Exception ex) {
                        logger.error("error retrieving Day Price Vol {} : {} ", url, ex);
                    }
                    finally {

                    }
                    return pass;
                }

            });
            fs.add(f);

            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        return fs;
    }

    public static void main(String[] args)
    {
        PriceVolumeRetrieval r = new PriceVolumeRetrieval();
        List<String> stocks = new ArrayList<>();
        stocks.add("sh600055");
        stocks.add("sh600019");
        stocks.add("sh600620");
        stocks.add("sz000415");
        r.capture(CalendarUtil.getCalendar(2014, 10, 30), CalendarUtil.getCalendar(2014, 10, 30), stocks);
    }

}
