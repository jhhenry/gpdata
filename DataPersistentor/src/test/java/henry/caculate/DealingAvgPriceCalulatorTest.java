package henry.caculate;

import java.text.ParseException;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import henry.calculate.DealingAvgPriceCalculator;
import henry.commons.CalendarUtil;

public class DealingAvgPriceCalulatorTest {
	@Test
	public void test() throws ParseException
	{
		DealingAvgPriceCalculator cal = new DealingAvgPriceCalculator("600459", new DateTime(CalendarUtil.getDashDate().parse("2017-4-24")), new DateTime(CalendarUtil.getDashDate().parse("2017-4-28")));
		double ret = cal.calculate();
		Assert.assertEquals(ret, 21.536611478832834);
	}
	
	@Test
	public void test2() throws ParseException
	{
		DateTime start = new DateTime(CalendarUtil.getDashDate().parse("2017-4-27"));
		DateTime end = new DateTime(CalendarUtil.getDashDate().parse("2017-4-27"));
		DealingAvgPriceCalculator cal = new DealingAvgPriceCalculator("600459", start, end);
		double ret = cal.calculate();
		Assert.assertEquals(ret, 21.536611478832834);
	}
}
