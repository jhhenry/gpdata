package henry.commons.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HTTPConnector
{
    private static final Logger logger = LogManager.getLogger();
    private String url;
    private static boolean failed = false;

    static {
        try {
        	probeWebsite();
        }
        catch (IOException e) {
            logger.warn("failed to directly connect to sina. Maybe proxy need be set first");
            System.getProperties().setProperty("http.proxyHost", "cn-proxy.jp.oracle.com");
            System.getProperties().setProperty("http.proxyPort", "80");
            try {
            	probeWebsite();
            }
            catch (IOException ex) {
                failed = true;
            }
        }

    }
    
    private static void probeWebsite() throws IOException
    {
    	URL u;
    	int tryCount = 3;
    	IOException e = null;
    	while(tryCount > 0) {
    		try {

    			u = new URL("http://www.sina.com.cn");
    			u.openStream();
    			failed = false;
    		}
    		catch (IOException ex) {
    			logger.error("failed to connect to sina. check your system network settings and status");
    			failed = true;
    			e = ex;
    		}
    		tryCount--;
    	}
    	if (failed) {
    		throw e;
    	}
    }
    
    public HTTPConnector(String url)
    {
        super();
        this.url = url;
    }

    public InputStream connect() throws IOException
    {
        if (failed) {
            throw new IOException("failed to connect to sina. check your system network settings and status");
        }
        URL u = new URL(url);
        logger.trace("connecting to: {}", url);
        URLConnection c = u.openConnection();
        c.setConnectTimeout(20000);
        c.setReadTimeout(30000);
        return c.getInputStream(); // $NON-NLS
    }

    public static void main(String[] args) throws IOException
    {
        new HTTPConnector("http://www.163.com").connect();
    }
}
