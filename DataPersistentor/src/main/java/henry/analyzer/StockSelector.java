package henry.analyzer;

import henry.analyzer.PhaseReport.PhaseType;
import henry.commons.CalendarUtil;
import henry.persistent.BasicDailyQuote;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dailyRun.Stocks;

public class StockSelector
{
    static final Logger logger = LogManager.getLogger();

    public boolean select(String stock)
    {
        StockPhaseAnalyzer ana = new StockPhaseAnalyzer(stock, CalendarUtil.getDate(2014, 10, 15), 15);

        PhaseReport r = ana.analyze();
        PhaseReport recent = r.getMostRightPhase();
        if (recent.getType() != PhaseType.SLOW_DROP) { // && recent.getType() != PhaseType.STABLE
            return false;
        }
        List<BasicDailyQuote> quotes = recent.getQuotes();
        if (quotes == null || quotes.size() < 3) {
            return false;
        }
        quotes = quotes.subList(quotes.size() - 3, quotes.size());
        double lastT = 0.0;
        BasicDailyQuote lastQ = null;
        for (BasicDailyQuote q : quotes) {
            // logger.error(q);
            // logger.error("turnover: {}" + q.getTurnover());
            if (Math.abs(q.getClosing() - q.getOpening()) / q.getOpening() > 0.02) {
                return false;
            }
            // if (q.getTurnover() < 1.5) {
            // return false;
            // }
            if (lastT > 0) {
                double p = (lastT - q.getTurnover()) / lastT;
                if (p < 0 || p > 0.3) {
                    return false;
                }
            }
            lastQ = q;
            lastT = q.getTurnover();
        }

        return true;
    }

    public static void main(String[] args)
    {
        // double p = 0.25;
        // System.out.println(p < 0 || p > 0.3);
        // p = -0.25;
        // System.out.println(p < 0 || p > 0.3);
        // p = -0.5;
        // System.out.println(p < 0 || p > 0.3);
        // p = 0.5;
        // System.out.println(p < 0 || p > 0.3);

        List<String> stocks = Stocks.getAllStocksNoPrefix();
        StockSelector s = new StockSelector();
        // System.out.print(s.select("601099"));
        for (String stock : stocks) {
            if (s.select(stock)) {
                System.out.println(stock);
            }
        }

    }
}
