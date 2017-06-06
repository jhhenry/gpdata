package henry.html.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stringtemplate.v4.ST;

import henry.commons.CalendarUtil;
import henry.commons.network.HTTPConnector;
import henry.persistent.BasicDailyQuote;

public class DailyQuoteFromSohu
{
    private static final Logger logger = LogManager.getLogger(DailyQuoteFromSohu.class.getName());
    private static final String URL_TEMPLATE = "http://q.stock.sohu.com/hisHq?code=cn_<stock>&start=<start>&end=<end>&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";
    public static Pattern DAILY_QUOTE_PATTERN = Pattern
            .compile("\\[\"(\\d{4}-\\d{2}-\\d{2})\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.\\-]+)\",\"([0-9.%\\-]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.]+)\",\"([0-9.%]+)\"\\]");

    public static void getDaily(Date start, Date end, String stock, IStringVisitor v)
    {
        ST urlT = new ST(URL_TEMPLATE);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        urlT.add("stock", stock);
        urlT.add("start", sdf.format(start));
        urlT.add("end", sdf.format(end));
        String url = urlT.render();
        StringBuilder content = new StringBuilder(1000);
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
        }
        catch (MalformedURLException e) {
            logger.catching(e);
            logger.error("url invalid: {}", url);
        }
        catch (IOException e) {
            logger.catching(e);
            logger.error("conneting failed: {} ", url);
        }

    }
    
    public static List<BasicDailyQuote> getDaily(Date start, Date end, String stock)
    {
    	class Capturer implements IStringVisitor {
    		private final List<BasicDailyQuote> quotes = new ArrayList<>();
			@Override
			public void onString(String str)
			{
				List<BasicDailyQuote> ret = getQuotes();
				Matcher m = DAILY_QUOTE_PATTERN.matcher(str);
				while (m.find()) {
					int c = m.groupCount();
					if (c < 10)
						continue;
					String dateStr = m.group(1);
					double open = Double.valueOf(m.group(2));
					double close = Double.valueOf(m.group(3));
					double gap = Double.valueOf(m.group(4));
					double lowest = Double.valueOf(m.group(6));
					double highest = Double.valueOf(m.group(7));
					long vol = Long.valueOf(m.group(8));
					double deal = Double.valueOf(m.group(9));
					String t = m.group(10);
					double turnover = Double.valueOf(t.substring(0, t.length() - 1));
					logger.trace("date {}, open {}, close {}, gap {}, lowest {}, highest {}, vol {}, deal {}, turnover {}", dateStr, open, close, gap, lowest, highest, vol, deal, turnover);
					try {
						Date d = CalendarUtil.getFormattedDate(dateStr, "-");
						BasicDailyQuote r = new BasicDailyQuote();
						r.setDate(d);
						r.setStock(stock);
						r.setOpening(open);
						r.setClosing(close);
						r.setGap(gap);
						r.setLowest(lowest);
						r.setHighest(highest);
						r.setVol(vol);
						r.setTotalDealPrice(deal);
						r.setTurnover(turnover);
						ret.add(r);
					}
					catch (Exception e) {
						logger.error("", e);
					}
				}
				
			}
			public List<BasicDailyQuote> getQuotes()
			{
				return quotes;
			}
    		
    	}
    	Capturer cp = new Capturer();
    	getDaily(start, end, stock, cp);
    	return cp.getQuotes();
    }

}
