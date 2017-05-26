package henry.query;

import henry.analyzer.PhaseReport;

public interface IStockFilter
{
    /**
     * Return true if the stock should be filtered.
     * 
     * @return
     */
    boolean filter(String stock, PhaseReport report);
}
