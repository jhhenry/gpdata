package henry.html.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stringtemplate.v4.ST;

import henry.commons.CalendarUtil;
import henry.commons.network.HTTPConnector;
import henry.persistent.BasicWeeklyQuote;

public class WeeklyQuoteFromSohu implements IStringVisitor
{
	private static final int STR_INIT_CAPACITY = 60 * 1024;
	private static final Logger logger = LogManager.getLogger();
	private static final String URL_TEMPLATE = "http://hq.stock.sohu.com/cn/<stock_last3>/cn_<stock>-7_1.html";
	private static final Pattern WEEKLY_QUOTE_PATTERN = Pattern.compile("\\['(\\d{8})','(\\d+\\.\\d+)','(\\d+\\.\\d+)','(\\d+\\.\\d+)','(\\d+\\.\\d+)','(\\d+)','(\\d+)','(\\d+\\.\\d+)%','(-?\\d+\\.\\d+)%','(-?\\d+\\.\\d+)','(\\d{8})'\\]");
	//("\\['\\d{8}','(\\d+\\.\\d+)','(\\d+\\.\\d+)','(\\d+\\.\\d+)','(\\d+\\.\\d+)','(\\d+)','(\\d+)','(\\d+\\.\\d+)%','(-?\\d+\\.\\d+)%','(-?\\d+\\.\\d+)',бо(\\d{8})'\\]");

	public static void getWeekly(String stock, IStringVisitor v) throws Exception {
		logger.trace("Capturing weekly k of " + stock);
		ST urlT = new ST(URL_TEMPLATE);
		urlT.add("stock", stock);
		urlT.add("stock_last3", stock.substring(stock.length() - 3));
		String url = urlT.render();
		StringBuilder content = new StringBuilder(STR_INIT_CAPACITY);
		try (InputStream is = new HTTPConnector(url).connect();) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			String line = bf.readLine();
			while (line != null) {
				content.append(line);
				line = bf.readLine();
			}
			String contentStr = content.toString();
			logger.trace(contentStr);
			v.onString(contentStr);
		} catch (MalformedURLException e) {
			logger.catching(e);
			logger.error("url invalid: {}", url);
			throw e;
		} catch (IOException e) {
			logger.catching(e);
			logger.error("conneting failed: {} ", url);
			throw e;
		}

	}
	
	public static List<BasicWeeklyQuote> capture(String stock) throws Exception
	{
		WeeklyQuoteFromSohu capture = new WeeklyQuoteFromSohu(stock);
		getWeekly(stock, capture);
		return capture.getKs();
		
	}
	
	private final String stock;
	
	private List<BasicWeeklyQuote> ks = new ArrayList<>();
	
	public WeeklyQuoteFromSohu(String s)
	{
		stock = s;
	}

	@Override
	public void onString(String str) {
		Matcher m = WEEKLY_QUOTE_PATTERN.matcher(str);
		while(m.find())
		{
			BasicWeeklyQuote k = new BasicWeeklyQuote();
			try {
				k.setStock(stock);
				k.setEndDate(CalendarUtil.getSimplestDateFormat().parse(m.group(1)));
				k.setOpening(Double.parseDouble(m.group(2)));
				k.setClosing(Double.parseDouble(m.group(3)));
				k.setHighest(Double.parseDouble(m.group(4)));
				k.setLowest(Double.parseDouble(m.group(5)));
				k.setVol(Long.parseLong(m.group(6)));
				k.setTotalDealPrice(Long.parseLong(m.group(7)));
				k.setTurnover(Double.parseDouble(m.group(8)));
				k.setGapPercentage(Double.parseDouble(m.group(9)));
				k.setGap(Double.parseDouble(m.group(10)));
				k.setStartDate(CalendarUtil.getSimplestDateFormat().parse(m.group(11)));
				getKs().add(k);
			} catch (ParseException e) {
				logger.catching(e);
			}
		}
	}

	public List<BasicWeeklyQuote> getKs() {
		return ks;
	}

	public void setKs(List<BasicWeeklyQuote> ks) {
		this.ks = ks;
	}

}
