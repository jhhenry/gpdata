package henry.query;

import henry.analyzer.PhaseReport;
import henry.persistent.BasicDailyQuote;

public class ScaleFilter implements IStockFilter
{
    private double maxScale = Double.MAX_VALUE;

    public ScaleFilter(double max)
    {
        maxScale = max;
    }

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        if (report == null || report.getDuration() <= 0)
            return true;
        BasicDailyQuote q = report.getQuotes().get(0);
        return q.getTotalDealPrice() * 100 / q.getTurnover() > maxScale * 10000.0;
    }

}
