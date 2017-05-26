package henry.query;

import henry.analyzer.PhaseReport;

public class NDropFilter implements IStockFilter
{

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        if (report == null)
            return true;

        return true;
    }
}
