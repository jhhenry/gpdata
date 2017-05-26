package henry.query;

import henry.analyzer.PhaseReport;

import java.util.ArrayList;
import java.util.List;

public class OrFilter implements IStockFilter
{
    private List<IStockFilter> filters = new ArrayList<>();

    public OrFilter addFilter(IStockFilter f)
    {
        filters.add(f);
        return this;
    }

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        for (IStockFilter f : filters) {
            if (!f.filter(stock, report)) {
                return false;
            }
        }
        return true;
    }

}
