package henry.analyzer;

import henry.commons.CalendarUtil;
import henry.persistent.DayPriceVol;
import henry.persistent.DayPriceVolService;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.testng.annotations.Test;

import dailyRun.Stocks;

public class TestDailyStatisticAnalyer
{
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("DayPriceVolService");
    static EntityManager em = emf.createEntityManager();
    static DayPriceVolService service = new DayPriceVolService(em);

    @Test(enabled = false)
    public void test()
    {
        String stock = "sh600651";
        List<DayPriceVol> vols = service.selectByStockDate(stock, CalendarUtil.getDate(2014, 9, 16), CalendarUtil.getDate(2014, 9, 22));

        DailyStatisticAnalyer ana = new DailyStatisticAnalyer();
        Map<Integer, Integer> d = ana.aggregatePercentageByPrice(vols);
        List<Integer> prices = new ArrayList<>();// .toArray(new Integer[]{});
        prices.addAll(d.keySet());
        Collections.sort(prices);

        for (Integer p : prices) {
            System.out.println(p + "  " + d.get(p));
        }
    }

    static ExecutorService exe = Executors.newFixedThreadPool(10);

    @Test
    public void testReport() throws Exception
    {
        List<String> stocks = Stocks.getAllStocks();// Arrays.asList(new
                                                    // String[] { "sh600651",
                                                    // "sh600000" });
        // Stocks.getAllStocks();
        List<Future<Void>> fs = new ArrayList<>();

        final FileWriter fw = new FileWriter(new File("d:/allStocks_Statistic_10_27.csv"));
        fw.write("stock,dev,dev/mean, mean, skewness, variance, gap\r\n");
        for (final String stock : stocks) {
            Future<Void> f = exe.submit(new Callable<Void>()
            {

                @Override
                public Void call() throws Exception
                {
                    DailyStatisticAnalyer ana = new DailyStatisticAnalyer();
                    ana.generateStatistics(stock, CalendarUtil.getDate(2014, 10, 23), CalendarUtil.getDate(2014, 10, 27), fw);
                    return null;
                }

            });
            fs.add(f);
        }
        for (Future<Void> f : fs) {
            f.get(3600, TimeUnit.SECONDS);
        }
        fw.flush();
        fw.close();
    }

}
