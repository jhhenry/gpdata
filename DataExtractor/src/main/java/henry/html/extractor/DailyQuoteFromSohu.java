package henry.html.extractor;

import henry.commons.network.HTTPConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stringtemplate.v4.ST;

public class DailyQuoteFromSohu
{
    private static final Logger logger = LogManager.getLogger(DailyQuoteFromSohu.class.getName());
    private static final String URL_TEMPLATE = "http://q.stock.sohu.com/hisHq?code=cn_<stock>&start=<start>&end=<end>&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";

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

}
