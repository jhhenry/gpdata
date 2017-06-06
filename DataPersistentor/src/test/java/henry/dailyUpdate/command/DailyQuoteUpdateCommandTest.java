package henry.dailyUpdate.command;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import henry.html.extractor.DailyQuoteFromSohu;
import henry.persistent.BasicDailyQuote;
import henry.persistent.BasicDailyQuoteService;
import henry.persistent.EMProvider;
import henry.test.utils.ExampleQuotes;
import mockit.Expectations;

public class DailyQuoteUpdateCommandTest {
	private final EntityManager em = EMProvider.getEMTest();
	private final BasicDailyQuoteService svr = new BasicDailyQuoteService(em);
	private final String STOCK_ID = DailyQuoteUpdateCommandTest.class.getCanonicalName();
	
	@BeforeMethod
	public void clearRecords()
	{
		em.getTransaction().begin();
		svr.deleteAll(STOCK_ID);
		em.getTransaction().commit();
	}

	@Test
	public void testNoData() {
		int recordsCount = 20;
		final List<BasicDailyQuote> qs = ExampleQuotes.getDailyQuotes(STOCK_ID, recordsCount);
		
		new Expectations(DailyQuoteFromSohu.class) {
			{
				DailyQuoteFromSohu.getDaily((Date)any, (Date)any, STOCK_ID); result = qs;
			}
		};
		DailyQuoteUpdateCommand cmd = new DailyQuoteUpdateCommand(STOCK_ID, svr);
		cmd.run();
		
		List<BasicDailyQuote> result = svr.getBasicDailyQuotes(STOCK_ID, qs.get(0).getDate(), qs.get(qs.size() - 1).getDate());
		Assert.assertEquals(result.size(), recordsCount);
	}
	
	@Test
	public void testInsertToday()
	{
		int recordsCount = 20;
		final List<BasicDailyQuote> qs = ExampleQuotes.getDailyQuotes(STOCK_ID, recordsCount);
		final List<BasicDailyQuote> oldOnes = qs.subList(0, qs.size() - 1);
		final List<BasicDailyQuote> today = qs.subList(qs.size() - 1, qs.size());
		svr.persist(oldOnes);
		
		new Expectations(DailyQuoteFromSohu.class) {
			{
				DailyQuoteFromSohu.getDaily((Date)any, (Date)any, STOCK_ID); result = today;
			}
		};
		
		DailyQuoteUpdateCommand cmd = new DailyQuoteUpdateCommand(STOCK_ID, svr);
		cmd.run();
		List<BasicDailyQuote> result = svr.getAllBasicDailyQuotes(STOCK_ID);
		Assert.assertEquals(result.size(), recordsCount);
		
		BasicDailyQuote d0 = result.get(result.size() - 1);
		Assert.assertEquals(d0.getDate(), today.get(0).getDate());
		Assert.assertEquals(d0, today.get(0));
	}
}
