package henry.persistent;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicWeeklyQuoteServiceTest extends AbstractWeekQuoteServiceTest
{
	private static DateTime dt = DateTime.now();
	private static EntityManager em = EMProvider.getEMTest();
	private static final String TEST_STOCK_ID = "BasicWeeklyQuoteServiceTest";

	@Test
	public void testCreate()
	{
		BasicWeeklyQuoteService srv = new BasicWeeklyQuoteService(em);
		em.getTransaction().begin();
		{
			srv.createRecord(dt.toDate(), dt.plusDays(5).toDate(), TEST_STOCK_ID, 0, 1.0, 0.5, 10.0, 0, 1.5, 222, 222,
					2.3);
		}
		em.getTransaction().commit();
		List<BasicWeeklyQuote> records = srv.getBasicWeeklyQuotes(TEST_STOCK_ID, dt.toDate(), dt.toDate());
		Assert.assertEquals(1, records.size());
	}
	
	@Test(dependsOnMethods="testCreate", expectedExceptions=javax.persistence.RollbackException.class)
	public void testPrimaryKey()
	{
		BasicWeeklyQuoteService srv = new BasicWeeklyQuoteService(em);
		try {
			em.getTransaction().begin();
			{
				srv.createRecord(dt.toDate(), dt.plusDays(6).toDate(), TEST_STOCK_ID, 0.9, 1.0, 0.5, 10.0, 0, 1.5, 111, 222,
						2.3);
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			throw ex;
		} finally {
			List<BasicWeeklyQuote> records = srv.getBasicWeeklyQuotes(TEST_STOCK_ID, dt.toDate(), dt.toDate());
			Assert.assertEquals(1, records.size());
		}
	}

	@Override
	String getStockID()
	{
		return TEST_STOCK_ID;
	}
}
