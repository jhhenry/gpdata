package henry.query;

import henry.analyzer.PhaseReport;
import henry.analyzer.StockPhaseAnalyzer;
import henry.commons.CalendarUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFilter
{
    /**
     * Return the retained stocks.
     * 
     * @param stocks
     * @param f
     * @return
     */
    public Map<String, PhaseReport> filter(List<String> stocks, IStockFilter f)
    {
        Map<String, PhaseReport> retained = new HashMap<>();
        // 1. get stock info
        // 2. filter by conditions
        for (String stock : stocks) {
            // filtered.ad
            PhaseReport latest = StockPhaseAnalyzer.getLatestReport(stock, CalendarUtil.getToday().getTime(), 10);
            if (latest == null)
                continue;
            if (!f.filter(stock, latest)) {
                retained.put(stock, latest);
            }
        }
        return retained;
    }

    public Map<String, PhaseReport> filter(Map<String, PhaseReport> stocks, IStockFilter f)
    {
        Map<String, PhaseReport> retained = new HashMap<>();
        // 1. get stock info
        // 2. filter by conditions
        for (String stock : stocks.keySet()) {
            PhaseReport report = stocks.get(stock);
            // filtered.ad
            if (report == null)
                continue;
            if (!f.filter(stock, report)) {
                retained.put(stock, report);
            }
        }
        return retained;
    }

}
