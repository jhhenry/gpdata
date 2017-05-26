package henry.persistent;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import henry.commons.CalendarUtil;

public abstract class AbstractWeekQuoteServiceTest
{
	private static EntityManager em = EMProvider.getEMTest();
	
	@BeforeClass
	public void setup()
	{
		BasicWeeklyQuoteService srv = new BasicWeeklyQuoteService(em);

		clearRecords(srv);
	}

	private void clearRecords(BasicWeeklyQuoteService srv)
	{
		em.getTransaction().begin();
		srv.deleteWeeklyQuotes(getStockID(), null, null);
		em.getTransaction().commit();
		List<BasicWeeklyQuote> records = srv.getBasicWeeklyQuotes(getStockID(), CalendarUtil.getDate(1970, 1, 1), DateTime.now().plusYears(15).toDate());
		Assert.assertEquals(records.size(), 0);
	}

	abstract String getStockID();
	
	@AfterClass
	public void tearDown()
	{
		BasicWeeklyQuoteService srv = new BasicWeeklyQuoteService(em);
		clearRecords(srv);
	}
}
