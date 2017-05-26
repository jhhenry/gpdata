package henry.analyzer;

import henry.commons.CalendarUtil;

import java.text.DateFormat;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PhaseTest
{

    @Test
    public void testAnalyzing_1()
    {
        DateFormat df = CalendarUtil.getDashDate();

        PhaseReport latest = StockPhaseAnalyzer.getLatestReport("000882", CalendarUtil.getDate(2014, 10, 16), 30);

        Assert.assertEquals(df.format(latest.getStart()), "2014-10-14");
        Assert.assertEquals(df.format(latest.getEnd()), "2014-10-16");

    }

    @Test
    public void testAnalyzing_2()
    {
        DateFormat df = CalendarUtil.getDashDate();

        PhaseReport latest = StockPhaseAnalyzer.getLatestReport("600361", CalendarUtil.getDate(2014, 10, 15), 30);
        Assert.assertEquals(df.format(latest.getStart()), "2014-09-26");
        Assert.assertEquals(df.format(latest.getEnd()), "2014-10-15");

    }

    @Test
    public void testAnalyzing_3()
    {
        DateFormat df = CalendarUtil.getDashDate();

        PhaseReport latest = StockPhaseAnalyzer.getLatestReport("600555", CalendarUtil.getDate(2014, 10, 13), 30);
        Assert.assertEquals(df.format(latest.getStart()), "2014-10-08");
        Assert.assertEquals(df.format(latest.getEnd()), "2014-10-13");

    }

    @Test
    public void testAnalyzing_4()
    {
        DateFormat df = CalendarUtil.getDashDate();

        PhaseReport latest = StockPhaseAnalyzer.getLatestReport("603168", CalendarUtil.getDate(2014, 10, 16), 30);
        Assert.assertEquals(df.format(latest.getStart()), "2014-10-13");
        Assert.assertEquals(df.format(latest.getEnd()), "2014-10-16");

    }

    @Test
    public void testAnalyzing_5()
    {
        DateFormat df = CalendarUtil.getDashDate();

        PhaseReport latest = StockPhaseAnalyzer.getLatestReport("600692", CalendarUtil.getDate(2014, 10, 16), 30);
        Assert.assertEquals(df.format(latest.getStart()), "2014-10-08");
        Assert.assertEquals(df.format(latest.getEnd()), "2014-10-16");

    }

}
