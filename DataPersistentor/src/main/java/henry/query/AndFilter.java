package henry.query;

import henry.analyzer.PhaseReport;

import java.util.ArrayList;
import java.util.List;

public class AndFilter implements IStockFilter
{
    private List<IStockFilter> filters = new ArrayList<>();

    public AndFilter(List<IStockFilter> f)
    {
        filters.addAll(f);
    }

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        for (IStockFilter f : filters) {
            if (f.filter(stock, report)) {
                return true;
            }
        }
        return false;
    }

}
