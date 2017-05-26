package henry.persistent;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import henry.commons.CalendarUtil;
import henry.html.extractor.WeeklyQuoteFromSohu;

public class WeeklyQuoteCaptureTest extends AbstractWeekQuoteServiceTest
{
	private static final String TEST_STOCK_ID = "002769";
	private static EntityManager em = EMProvider.getEMTest();

	@Test
	public void testCapture() throws Exception
	{
		List<BasicWeeklyQuote> ws = WeeklyQuoteFromSohu.capture(getStockID());
		BasicWeeklyQuoteService srv = new BasicWeeklyQuoteService(em);
		em.getTransaction().begin();
		for (BasicWeeklyQuote week : ws) {
			srv.create(week);
		}
		em.getTransaction().commit();
		
		DateTime dt = DateTime.now();
		List<BasicWeeklyQuote> records = srv.getBasicWeeklyQuotes(getStockID(), CalendarUtil.getCalendar(2015, 6, 29).getTime(), dt.toDate());
		Assert.assertTrue(records.size() > 90);
	}

	@Override
	String getStockID()
	{
		return TEST_STOCK_ID;
	}
}
