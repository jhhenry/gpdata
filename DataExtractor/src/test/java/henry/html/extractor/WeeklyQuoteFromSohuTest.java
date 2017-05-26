package henry.html.extractor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import henry.commons.CalendarUtil;
import henry.persistent.BasicWeeklyQuote;

public class WeeklyQuoteFromSohuTest {
	@Test
	public void test() throws Exception {
		WeeklyQuoteFromSohu.getWeekly("002769", new IStringVisitor() {
			@Override
			public void onString(String str) {
				Assert.assertTrue(str != null && str.contains("quote_wk_all"));
			}

		});
	}

	@Test(enabled = false)
	public void testCapture() throws Exception {
		List<BasicWeeklyQuote> ks = WeeklyQuoteFromSohu.capture("002769");
		Assert.assertEquals(ks.size(), 93);
	}

	@Test
	void testCaptureString() throws Exception {
		WeeklyQuoteFromSohu c = new WeeklyQuoteFromSohu( "002769");
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("weekly_k_002769.txt");
				BufferedReader bf = new BufferedReader(new InputStreamReader(is));) {

			String line = bf.readLine();
			c.onString(line);
			List<BasicWeeklyQuote> ks = c.getKs();
			Assert.assertEquals(ks.size(), 93);
			BasicWeeklyQuote k1 = ks.get(0);
			Assert.assertEquals(k1.getStock(),  "002769");
			Assert.assertEquals(CalendarUtil.getSimplestDateFormat().format(k1.getEndDate()), "20170428");
			Assert.assertEquals(k1.getOpening(), 18.0d);
			Assert.assertEquals(k1.getClosing(), 17.21d);
			Assert.assertEquals(k1.getHighest(), 18.00d);
			Assert.assertEquals(k1.getLowest(), 16.4d);
			//'96102','16439','5.05%','-4.39%','-0.79','20170424'
			Assert.assertEquals(k1.getVol(), 96102l);
			Assert.assertEquals(k1.getTotalDealPrice(), 16439d);
			Assert.assertEquals(k1.getTurnover(), 5.05d);
			Assert.assertEquals(k1.getGapPercentage(), -4.39d);
			Assert.assertEquals(k1.getGap(), -0.79d);
			Assert.assertEquals(CalendarUtil.getSimplestDateFormat().format(k1.getStartDate()), "20170424");
			//['20150703','7.44','11.93','11.93','7.44','2281','1310','1.23%','112.28%','6.31','20150629']"
			BasicWeeklyQuote k = ks.get(ks.size() - 1);
			Assert.assertEquals(CalendarUtil.getSimplestDateFormat().format(k.getEndDate()), "20150703");
			Assert.assertEquals(k.getOpening(), 7.44d);
			Assert.assertEquals(k.getGapPercentage(), 112.28d);
			
			
		}
	}
}
