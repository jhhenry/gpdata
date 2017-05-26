package henry.query;

import henry.analyzer.PhaseReport;
import henry.persistent.BasicDailyQuote;

import java.util.Collections;
import java.util.List;

public class TempAdjustFilter implements IStockFilter
{

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        List<BasicDailyQuote> quotes = (report.getQuotes());
        Collections.reverse(quotes);
        while (quotes.size() < 4) {
            report = report.getLastPhase();
            if (report == null)
                break;
            List<BasicDailyQuote> q = (report.getQuotes());
            Collections.reverse(q);
            quotes.addAll(q);
        }
        quotes = quotes.subList(0, Math.min(quotes.size(), 4));

        // find a rise
        // check its subsequence satisfy the conditions
        int riseIndex = 0;
        for (int i = 0; i < quotes.size(); i++) {
            BasicDailyQuote q = quotes.get(i);
            double gap = q.getGap();
            if (gap > 0 && gap / (q.getClosing() - gap) >= 0.03) {
                riseIndex = i;
                break;
            }
        }
        for (int i = riseIndex - 1; i >= 0; i--) {
            // 2、股价以一根带长上影小阴阳K线报收，收盘时仍然保持在1%-3%左右的涨跌幅。
            // 3、当天量比1倍以上，换手率在5%以内，振幅在7%以上。
            BasicDailyQuote q = quotes.get(i);
            double gap = q.getGap();
            boolean slightChange = Math.abs(gap) / (q.getClosing() - gap) < 0.03;
            boolean bigVibrate = (q.getHighest() - q.getLowest()) / q.getLowest() >= 0.06;
            boolean smallTurnOver = q.getTurnover() < quotes.get(riseIndex).getTurnover() || q.getTurnover() < 5.0;
            return !(slightChange && bigVibrate && smallTurnOver);
        }
        return true;
    }
}
