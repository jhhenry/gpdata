package henry.query;

import henry.analyzer.PhaseReport;
import henry.analyzer.StockPhaseAnalyzer;
import henry.commons.CalendarUtil;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleFilterTest
{
    @Test
    public void testScaleFilter()
    {
        IStockFilter f = new ScaleFilter(200.0);
        String stock = "603168";
        Date date = CalendarUtil.getDate(2014, 10, 17);
        PhaseReport r = StockPhaseAnalyzer.getLatestReport("603168", date, 2);
        Assert.assertEquals(true, f.filter(stock, r));

        r = StockPhaseAnalyzer.getLatestReport("600600", date, 2);
        Assert.assertEquals(false, f.filter(stock, r));
    }

    @Test
    public void testNRiseFilter()
    {
        IStockFilter f = new NRiseFilter();
        String stock = "600141";
        Date date = CalendarUtil.getDate(2014, 10, 15);
        PhaseReport r = StockPhaseAnalyzer.getLatestReport(stock, date, 10);
        Assert.assertEquals(false, f.filter(stock, r));
    }
}
