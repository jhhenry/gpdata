package henry.query;

import henry.analyzer.PhaseReport;
import henry.analyzer.PhaseReport.PhaseType;
import henry.persistent.BasicDailyQuote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NRiseFilter implements IStockFilter
{
    int minDuration = 3;
    double tolerateDropPercentage = 0.01;// 1%
    double maxRiseGap = 0.06; // 6%

    public NRiseFilter()
    {

    }

    public NRiseFilter(int minDuration, double tolerateDropPercentage, double maxRiseGap)
    {
        super();
        this.minDuration = minDuration;
        this.tolerateDropPercentage = tolerateDropPercentage;
        this.maxRiseGap = maxRiseGap;
    }

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        List<BasicDailyQuote> resultQuotes = new ArrayList<>(minDuration);
        if (filter(report, resultQuotes)) {
            return true;
        }
        else {
            if (resultQuotes.size() < 1)
                return true;
            BasicDailyQuote first = resultQuotes.get(0);
            BasicDailyQuote last = resultQuotes.get(resultQuotes.size() - 1);
            double riseP = (first.getClosing() - last.getClosing()) / last.getClosing();
            return (riseP > maxRiseGap);
        }
    }

    private boolean filter(PhaseReport report, List<BasicDailyQuote> resultQuotes)
    {
        if (report == null)
            return true;
        PhaseType type = report.getType();
        if (type == PhaseType.SLOW_DROP || type == PhaseType.SHARP_DROP)
            return true;
        int duration = minDuration - resultQuotes.size();
        if (report.getDuration() <= duration) {
            return true;
        }
        if (type == PhaseType.SLOW_RISE || type == PhaseType.SHARP_RISE) {
            if (report.getDuration() >= duration) {
                return false;
            }
        }
        // if it is stable
        List<BasicDailyQuote> quotes = report.getQuotes();
        Collections.reverse(quotes);

        for (BasicDailyQuote q : quotes.subList(0, duration)) {
            double gap = q.getGap();
            if (gap < 0) {
                if (Math.abs(gap) / q.getClosing() > tolerateDropPercentage) {
                    break; // filtered
                }
                // 重心 未下移
                int lastQIndex = quotes.indexOf(q) + 1;
                if (lastQIndex < duration) {
                    BasicDailyQuote lastQ = quotes.get(lastQIndex);
                    double avg = (q.getClosing() + q.getOpening()) / 2.0;
                    double lastAvg = (lastQ.getClosing() + lastQ.getOpening()) / 2.0;
                    if (avg <= lastAvg) {
                        break; // filtered
                    }
                }
                else {
                    break; // filtered
                }
                if (q.getClosing() - q.getOpening() < ((q.getHighest() - q.getClosing()) + (q.getOpening() - q.getLowest()) * 0.8)) {
                    break; // filtered
                }
                resultQuotes.add(q);
            }
            else {
                resultQuotes.add(q);
            }
            if (resultQuotes.size() >= minDuration) {
                return false;
            }
        }
        return filter(report.getLastPhase(), resultQuotes);
    }
}
