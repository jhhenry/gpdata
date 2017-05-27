package henry.caculate;

import java.text.ParseException;
import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.Test;

import henry.calculate.DealingAvgPriceCalculator;

public class DealingAvgPriceCalulatorTest {
	@Test
	public void test() throws ParseException
	{
		DealingAvgPriceCalculator cal = new DealingAvgPriceCalculator("600459", LocalDate.of(2017, 4, 24), LocalDate.of(2017, 4, 25));
		double ret = cal.calculate();
		Assert.assertEquals(ret, 21.536611478832834);
	}
	
	@Test
	public void test2() throws ParseException
	{
		LocalDate start = LocalDate.of(2017, 4, 27);
		LocalDate end = LocalDate.of(2017, 4, 27);
		DealingAvgPriceCalculator cal = new DealingAvgPriceCalculator("600459", start, end);
		double ret = cal.calculate();
		Assert.assertEquals(ret, 21.536611478832834);
	}
}
