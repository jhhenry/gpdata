package henry.persistent.integrator;

import org.testng.annotations.Test;

public class WeeklyQuoteRetrivalTest
{
	@Test
	public void testNormalCase()
	{
		
	}
	
	@Test
	public void testException()
	{
		/*
		 * Exception 1: when there is network exception or server error, retry 3 times before success.
		 * In the case of the final failure, log the exception in the "exception" table in db.
		 * 
		 * Exception 2: 
		 */
		try {
			
		} catch (Exception ex) {
			
		}
	}
}
