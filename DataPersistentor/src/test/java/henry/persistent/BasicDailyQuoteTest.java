package henry.persistent;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.testng.Assert;
import org.testng.annotations.Test;

import dailyRun.DailyQuoteRetrieval;
import dailyRun.Stocks;
import henry.commons.CalendarUtil;

public class BasicDailyQuoteTest
{
	private static final EntityManager em = EMProvider.getEMTest();
	private final BasicDailyQuoteService svr = new BasicDailyQuoteService(em);
	private static final String STOCK_ID = "BasicDailyQuoteTest";
	private static final Date date = CalendarUtil.getDate(2014, 9, 30);
	
    @Test
    public void test()
    {
        createRecord();
        
        List<BasicDailyQuote> quotes = svr.getBasicDailyQuotes(STOCK_ID, date, date);
        Assert.assertNotNull(quotes);
        Assert.assertEquals(quotes.size(), 1);
        BasicDailyQuote basicDailyQuote = quotes.get(0);
		Assert.assertEquals(basicDailyQuote.getStock(), STOCK_ID);
        Assert.assertEquals(basicDailyQuote.getDate(), date);
        Assert.assertEquals(basicDailyQuote.getOpening(), 10.0);
        Assert.assertEquals(basicDailyQuote.getVol(), 11929292);
        Assert.assertEquals(basicDailyQuote.getTotalDealPrice(), 293939238.99);
        Assert.assertEquals(basicDailyQuote.getTurnover(), 1.2);
        Assert.assertEquals(basicDailyQuote.getGap(), 1.0);
        Assert.assertEquals(basicDailyQuote.getGapPercentage(), 1.0d/9.0d);
    }

	private void createRecord()
	{
		em.getTransaction().begin();
		svr.createRecord(date, STOCK_ID, 10.0, 10.0, 1.0, 9.8, 8.9, 11929292, 293939238.99, 1.2);
        em.getTransaction().commit();
	}
    
    @Test(dependsOnMethods="test", expectedExceptions=javax.persistence.RollbackException.class)
    public void testPrimaryKey()
    {
		try {
			createRecord();

		} catch (Exception ex) {
			List<BasicDailyQuote> quotes = svr.getBasicDailyQuotes(STOCK_ID, date, date);
			Assert.assertNotNull(quotes);
			Assert.assertEquals(quotes.size(), 1);
			throw ex;
		}
    }

    @Test(enabled = false)
    void getHistoricalQuoteFromSohu()
    {
        DailyQuoteRetrieval d = new DailyQuoteRetrieval("600651");
        d.getQuota(CalendarUtil.getDate(2014, 9, 26), CalendarUtil.getDate(2014, 9, 29));
    }

    @Test(enabled = false)
    void getAll()
    {
        List<String> stocks = Stocks.getAllStocksNoPrefix();
        new DailyQuoteRetrieval(null).getAll(CalendarUtil.getDate(2014, 10, 10), CalendarUtil.getDate(2014, 10, 10), stocks);
    }

}
