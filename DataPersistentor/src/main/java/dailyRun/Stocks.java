package dailyRun;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import henry.commons.CalendarUtil;

public class Stocks
{
    private static List<String> sz_stocks = new ArrayList<String>();
    private static List<String> sh_stocks = new ArrayList<String>();
    private static List<String> cy_stocks = new ArrayList<String>();
    private static List<String> all_stocks = new ArrayList<String>();
    static Pattern p = Pattern.compile("\\((.*)\\)");
    static List<String> stocks = null;
    static {
    	loadStocks();
        all_stocks = new ArrayList<String>();
        all_stocks.addAll(sh_stocks);
        all_stocks.addAll(sz_stocks);
        all_stocks.addAll(cy_stocks);

    }
    
    private static void loadStocks()
    {
    	int max_sh = 3999;
    	int max_sz = 2867;
    	int max_cy = 648;
    	for (int i = 1; i <= max_sh; i++) {
    		sh_stocks.add("60" + i);
    	}
    	
    	for (int i = 1; i <= max_sz; i++) {
    		sz_stocks.add("00" + i);
    	}
    	
    	for (int i = 1; i <= max_cy; i++) {
    		cy_stocks.add("300" + i);
    	}
    }

    public static List<String> getSZStocks(String prefix)
    {
        if (prefix == null)
            return sz_stocks;
        return prependPrefix(prefix, sz_stocks);
    }

    public static List<String> getSHStocks(String prefix)
    {
        if (prefix == null)
            return sh_stocks;
        return prependPrefix(prefix, sh_stocks);
    }

    private static List<String> prependPrefix(String prefix, List<String> stocks)
    {
        List<String> all_stocks = new ArrayList<String>();
        for (String s : stocks) {
            all_stocks.add(prefix + s);
        }
        return all_stocks;
    }

    public static List<String> getAllStocks()
    {
        List<String> all_stocks = new ArrayList<String>();
        all_stocks.addAll(getSHStocks("sh"));
        all_stocks.addAll(getSZStocks("sz"));
        return all_stocks;
    }

    public static List<String> getAllStocksNoPrefix()
    {
        return all_stocks;
    }

    public static List<String> getSampleStocks()
    {
        List<String> all_stocks = new ArrayList<String>();
        all_stocks.addAll(getSHStocks(null));
        return all_stocks.subList(0, 200);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        System.getProperties().setProperty("http.proxyHost", "cn-proxy.jp.oracle.com");
        System.getProperties().setProperty("http.proxyPort", "80");

        PriceVolumeRetrieval r = new PriceVolumeRetrieval();
        List<String> stocks = getAllStocks();// Arrays.asList(new
                                             // String[]{"sh601515",
                                             // "sz000910"});
        List<Future<Object>> fs = r.capture(CalendarUtil.getCalendar(2014, 9, 15), CalendarUtil.getCalendar(2014, 9, 19), stocks);

        for (Future<Object> f : fs) {
            f.get();
        }
    }
}
