package henry.query;

import henry.analyzer.PhaseReport;
import henry.persistent.BasicDailyQuote;

import java.util.Collections;
import java.util.List;

public class HugeTurnOverChange implements IStockFilter
{
    private int minMutiple = 6;

    public HugeTurnOverChange(int minMutiple)
    {
        super();
        this.minMutiple = minMutiple;
    }

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        int maxCount = 4;

        List<BasicDailyQuote> quotes = report.getMostRightPhase().getQuotes();
        BasicDailyQuote nextq = null;
        int count = 0;
        while (quotes != null && count < maxCount) {
            Collections.reverse(quotes);
            for (BasicDailyQuote q : quotes) {
                count++;
                if (nextq != null) {
                    if (nextq.getTurnover() >= q.getTurnover() * minMutiple) {
                        return q.getGap() / (q.getOpening() - q.getGap()) > 0.09 || (nextq.getGap() < 0 && Math.abs(nextq.getGap()) / q.getClosing() < 0.02);
                    }
                }
                nextq = q;
            }
            quotes = report.getLastPhase() != null ? report.getLastPhase().getQuotes() : null;
            report = report.getLastPhase();

        }
        return true;
    }

}
