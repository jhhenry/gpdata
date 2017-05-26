package henry.query;

import henry.analyzer.PhaseReport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import dailyRun.Stocks;

public class MainFilterTest
{
    static final Logger logger = LogManager.getLogger();
    static String[] s = new String[] { "601988" };

    @Test
    public void run()
    {
        IStockFilter f = makeFilter();
        MainFilter mf = new MainFilter();
        List<String> stocks = Stocks.getAllStocksNoPrefix();
        Arrays.asList(s);//

        Map<String, PhaseReport> retained = mf.filter(stocks, f);
        for (String stock : retained.keySet()) {
            logger.info(stock);
        }

    }

    private IStockFilter makeFilter()
    {
        List<IStockFilter> fs = new ArrayList<>();
        IStockFilter rise = new NRiseFilter();
        fs.add(new ScaleFilter(500));
        fs.add(rise);
        IStockFilter f = new AndFilter(fs);

        OrFilter orF = new OrFilter();
        orF.addFilter(new HugeTurnOverChange(4));
        orF.addFilter(f);
        return orF;
    }
}
