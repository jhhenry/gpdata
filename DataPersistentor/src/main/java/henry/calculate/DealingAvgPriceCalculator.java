package henry.calculate;

import java.time.LocalDate;
import java.util.List;

import henry.commons.DateUtils;
import henry.persistent.BasicDailyQuote;
import henry.persistent.BasicDailyQuoteService;
import henry.persistent.EMProvider;

public class DealingAvgPriceCalculator
{
	private final String stock;
	private final LocalDate start;
	private final LocalDate end;
	public DealingAvgPriceCalculator(String stock, LocalDate start, LocalDate end) {
		super();
		this.stock = stock;
		this.start = start;
		this.end = end == null ? LocalDate.now() : end;
		if (start.compareTo(end) > 0) {
			throw new AssertionError("Start date should be ealier than end date");
		}
	}
	
	public double calculate()
	{
//		List<Calendar> specifiedDates = new ArrayList<>();
//		LocalDate addedDate = new LocalDate(start);
//		while (addedDate != null && addedDate.isBeforeNow() && (addedDate.isBefore(end) || addedDate.isEqual(end))) {
//			if (addedDate.getDayOfWeek() <= 5) {
//				specifiedDates.add(addedDate.toCalendar(Locale.CHINA));
//			}
//			addedDate = addedDate.plusDays(1);
//		}
		BasicDailyQuoteService srv = new BasicDailyQuoteService(EMProvider.getEM());
		List<BasicDailyQuote> quotes = srv.getBasicDailyQuotes(stock, DateUtils.asDate(start), DateUtils.asDate(end));
		if (quotes != null) {
			double total = 0.0d;
			long totalShou = 0;
			for (BasicDailyQuote q : quotes) {
				total += q.getTotalDealPrice();
				totalShou += q.getVol();
			}
			return totalShou != 0 ? (total/totalShou)*100 : 0.0d;
		}
		
		
		return 0.0d;
	}
	
	
}
