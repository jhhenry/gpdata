package henry.dailyUpdate.command;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import henry.commons.CalendarUtil;
import henry.commons.DateUtils;
import henry.html.extractor.DailyQuoteFromSohu;
import henry.persistent.BasicDailyQuote;
import henry.persistent.BasicDailyQuoteService;

public class DailyQuoteUpdateCommand
{
	private static final Logger logger = LoggerFactory.getLogger(DailyQuoteUpdateCommand.class);
	private final String stockId;
	private final BasicDailyQuoteService srv;
	
	public DailyQuoteUpdateCommand(String stockId, BasicDailyQuoteService srv)
	{
		super();
		this.stockId = stockId;
		this.srv = srv;
	}

	public void run()
	{
		String stock  = stockId;
		try {
			Objects.requireNonNull(stock, "stock");
			List<BasicDailyQuote> unInsertedQuotes = retrieveLatestQuotes(stock);
			persisteQuotes(unInsertedQuotes);
		} catch (Throwable ex ){
			logger.error("Unexpected error.", ex);
		}
	}

	private List<BasicDailyQuote> retrieveLatestQuotes(String stock)
	{
		Date latestInDB = getLatestDateFromDB();
		LocalDate start;
		LocalDate end;
		if (latestInDB == null) {
			LocalDate dt = LocalDate.now();
			LocalDate oneYearAgo = dt.minusYears(1);
			start = oneYearAgo;
			end = dt;
		} else if (lagBehindToday(latestInDB)) {
			start = DateUtils.asLocalDate(latestInDB).plusDays(1);
			end = CalendarUtil.getLatestWorkDay();
		} else {
			return Collections.emptyList();
		}
		
		return DailyQuoteFromSohu.getDaily(DateUtils.asDate(start), DateUtils.asDate(end), stock);
	}
	
	private void persisteQuotes(List<BasicDailyQuote> unInsertedQuotes)
	{
		if (unInsertedQuotes != null && unInsertedQuotes.size() > 0) {
			srv.persist(unInsertedQuotes);
		}

	}

	private boolean lagBehindToday(Date latestInDB)
	{
		LocalDate latestWorkday = CalendarUtil.getLatestWorkDay();
		return latestWorkday.isAfter(DateUtils.asLocalDate(latestInDB));
	}

	private Date getLatestDateFromDB()
	{
		BasicDailyQuote q = srv.getLatestQuote(stockId);
		return q != null ? q.getDate() : null;
	}

}
