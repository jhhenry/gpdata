package henry.analyzer;

import henry.commons.CalendarUtil;
import henry.persistent.BasicDailyQuote;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestStatistics
{
    static List<Integer> data1 = new ArrayList<Integer>();

    static List<Integer> data2 = new ArrayList<Integer>();

    static List<Integer> data3 = new ArrayList<Integer>();

    static {
        data1.add(80);
        data1.add(18);
        data1.add(1);
        data1.add(1);

        data2.add(25);
        data2.add(25);
        data2.add(25);
        data2.add(25);

        data3.add(40);
        data3.add(30);
        data3.add(10);
        data3.add(5);
        data3.add(5);
        data3.add(5);
        data3.add(4);
        data3.add(1);

    }

    @Test
    public void test()
    {
        print(caculateConcentration(data1, 80, 1));

        print(caculateConcentration(data2, 80, 1));

        print(caculateConcentration(data3, 80, 1));
    }

    private static void print(Object o)
    {
        System.out.println(o);
    }

    public double caculateConcentration(List<Integer> percentages, int roof, int mulipler)
    {
        // Collections.sort(percentages);
        double count = 0;
        double total = 0;
        for (int p : percentages) {
            total += p;
            count++;
            if (total >= roof * mulipler) {
                break;
            }
        }
        double r = percentages.size() / count;
        return r;
    }

    @Test
    void testBoDong()
    {
        StockPhaseAnalyzer ana = new StockPhaseAnalyzer("601099", CalendarUtil.getDate(2014, 10, 14), 15);

        PhaseReport r = ana.analyze();
        PhaseReport recent = r.getMostRightPhase();

        List<BasicDailyQuote> quotes = recent.getQuotes();
        for (BasicDailyQuote q : quotes) {
            System.out.println(q);
        }
        DateFormat df = CalendarUtil.getDashDate();
        Assert.assertEquals(df.format(r.getStart()), "2014-09-12");
        Assert.assertEquals(df.format(r.getEnd()), "2014-09-15");

        Assert.assertNull(r.getLastPhase());

        r = r.getNextPhase();
        Assert.assertEquals(df.format(r.getStart()), "2014-09-16");
        Assert.assertEquals(df.format(r.getEnd()), "2014-09-17");

        r = r.getNextPhase();
        Assert.assertEquals(df.format(r.getStart()), "2014-09-18");
        Assert.assertEquals(df.format(r.getEnd()), "2014-09-22");

        r = r.getNextPhase();
        Assert.assertEquals(df.format(r.getStart()), "2014-09-23");
        Assert.assertEquals(df.format(r.getEnd()), "2014-09-25");

        r = r.getNextPhase();
        Assert.assertEquals(df.format(r.getStart()), "2014-09-26");
        Assert.assertEquals(df.format(r.getEnd()), "2014-10-10");
    }
}
