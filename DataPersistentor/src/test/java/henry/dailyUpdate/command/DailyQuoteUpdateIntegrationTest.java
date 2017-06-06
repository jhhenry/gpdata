package henry.dailyUpdate.command;

import java.util.List;

import javax.persistence.EntityManager;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import henry.persistent.BasicDailyQuote;
import henry.persistent.BasicDailyQuoteService;
import henry.persistent.EMProvider;

public class DailyQuoteUpdateIntegrationTest
{
	private final EntityManager em = EMProvider.getEMTest();
	private final BasicDailyQuoteService svr = new BasicDailyQuoteService(em);
	private final String STOCK_ID = "600519"; // chinese wine

	@BeforeMethod
	public void clearRecords()
	{
		em.getTransaction().begin();
		svr.deleteAll(STOCK_ID);
		em.getTransaction().commit();
	}

	@Test
	public void testFromScratch()
	{
		DailyQuoteUpdateCommand cmd = new DailyQuoteUpdateCommand(STOCK_ID, svr);
		cmd.run();
		List<BasicDailyQuote> result = svr.getAllBasicDailyQuotes(STOCK_ID);
		Assert.assertTrue(result.size() > 240);
	}
	
	
	
	
}
