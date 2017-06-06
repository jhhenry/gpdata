package henry.persistent;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dailyRun.DailyQuoteRetrieval;
import dailyRun.Stocks;
import henry.commons.CalendarUtil;
import henry.commons.DateUtils;

public class BasicDailyQuoteServiceTest
{
	private static final EntityManager em = EMProvider.getEMTest();
	private final BasicDailyQuoteService svr = new BasicDailyQuoteService(em);
	private static final String STOCK_ID = "BasicDailyQuoteTest";
	private static final Date date = CalendarUtil.getDate(2014, 9, 30);
	private static final long oneDay = 60 * 60 * 24 * 1000;
	
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
    
    @Test(expectedExceptions=javax.persistence.RollbackException.class)
    public void testPrimaryKey()
    {
    	createRecord();
		try {
			createRecord();

		} catch (Exception ex) {
			List<BasicDailyQuote> quotes = svr.getBasicDailyQuotes(STOCK_ID, date, date);
			Assert.assertNotNull(quotes);
			Assert.assertEquals(quotes.size(), 1);
			throw ex;
		}
    }
    
    @Test
    public void testGetLatestQuote()
    {
    	em.getTransaction().begin();
		svr.createRecord(new Date(date.getTime() + oneDay) , STOCK_ID, 10.0, 10.0, 1.0, 9.8, 8.9, 11929292, 293939238.99, 1.2);
        em.getTransaction().commit();
        
        BasicDailyQuote q = svr.getLatestQuote(STOCK_ID);
        Assert.assertNotNull(q);
        Assert.assertEquals(q.getDate(), new Date(date.getTime() + oneDay));
    }
    
    @Test
    public void testDelete()
    {
    	em.getTransaction().begin();
		Date date2 = new Date(date.getTime() + oneDay * 2);
		svr.createRecord(date2 , STOCK_ID, 10.0, 10.0, 1.0, 9.8, 8.9, 11929292, 293939238.99, 1.2);
		Date date3 = new Date(date.getTime() + oneDay * 3);
		svr.createRecord(date3 , STOCK_ID, 10.0, 10.0, 1.0, 9.8, 8.9, 11929292, 293939238.99, 1.2);
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        svr.delete(STOCK_ID, date2, date2);
        em.getTransaction().commit();
        List<BasicDailyQuote> q = svr.getBasicDailyQuotes(STOCK_ID, date2, date3);
        Assert.assertNotNull(q);
        Assert.assertEquals(q.size(), 1);
        em.getTransaction().begin();
        svr.delete(STOCK_ID, date2, date3);
        em.getTransaction().commit();
        List<BasicDailyQuote> q2 = svr.getBasicDailyQuotes(STOCK_ID, date2, date3);
        Assert.assertNotNull(q2);
        Assert.assertEquals(q2.size(), 0);
    }
    
    @Test
    public void testGetAllByStock()
    {
    	em.getTransaction().begin();
		Date date2 = new Date(date.getTime() + oneDay * 4);
		svr.createRecord(date2 , STOCK_ID, 10.0, 10.0, 1.0, 9.8, 8.9, 11929292, 293939238.99, 1.2);
		Date date3 = new Date(date.getTime() + oneDay * 5);
		svr.createRecord(date3 , STOCK_ID, 10.0, 10.0, 1.0, 9.8, 8.9, 11929292, 293939238.99, 1.2);
        em.getTransaction().commit();
        
        List<BasicDailyQuote> q = svr.getAllBasicDailyQuotes(STOCK_ID);
        Assert.assertNotNull(q);
        Assert.assertEquals(q.size()> 0, true);
    }
    
    @BeforeMethod
    @AfterClass
    public void clear()
    {
    	em.getTransaction().begin();
    	svr.deleteAll(STOCK_ID);
    	em.getTransaction().commit();
        List<BasicDailyQuote> q = svr.getBasicDailyQuotes(STOCK_ID, DateUtils.asDate(LocalDate.parse("1991-01-01")), DateUtils.asDate(LocalDate.now()));
        Assert.assertNotNull(q);
        Assert.assertEquals(q.size(), 0);
    }
    

    @Test(enabled = false)
    void getHistoricalQuoteFromSohu()
    {
        DailyQuoteRetrieval d = new DailyQuoteRetrieval("600651");
        d.persisteQuotes(CalendarUtil.getDate(2014, 9, 26), CalendarUtil.getDate(2014, 9, 29));
    }

    @Test(enabled = false)
    void getAll()
    {
        List<String> stocks = Stocks.getAllStocksNoPrefix();
        new DailyQuoteRetrieval(null).getAll(CalendarUtil.getDate(2014, 10, 10), CalendarUtil.getDate(2014, 10, 10), stocks);
    }

}
