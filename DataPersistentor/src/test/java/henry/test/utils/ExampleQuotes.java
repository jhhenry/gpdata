package henry.test.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import henry.commons.DateUtils;
import henry.persistent.BasicDailyQuote;

public class ExampleQuotes
{
	/**
	 * It returns "count" basic daily quotes with the specified "stock", dating from "count" - 1 days ago until today(inclusive).
	 * The first element of the retured list is the oldest one and the last element is the latest one, that is today.
	 * @param stock
	 * @param count
	 * @return
	 */
	public static List<BasicDailyQuote> getDailyQuotes(String stock, int count)
	{
		final List<BasicDailyQuote> qs = new ArrayList<>();
		LocalDate now = LocalDate.now();
		LocalDate daysAgo = now.minusDays(count - 1);
		double opening = 1.0;
		double closing = 1.05;
		double gap = 0.4;
		double lowest = 1.04;
		double highest = 1.07;
		long vol = 11111;
		double totalDealPrice = 11112.1;
		double turnover = 3.3;
		for (int i = 0; i < count; i++) {
			BasicDailyQuote r = new BasicDailyQuote();
	        r.setDate(DateUtils.asDate(daysAgo));
	        r.setStock(stock);
			r.setOpening(opening);
			r.setClosing(closing);
			r.setGap(gap);
			r.setLowest(lowest);
			r.setHighest(highest);
			r.setVol(vol);
			r.setTotalDealPrice(totalDealPrice);
			r.setTurnover(turnover);
	        daysAgo = daysAgo.plusDays(1);
	        qs.add(r);
		}
		return qs;
	}
	
	
}
