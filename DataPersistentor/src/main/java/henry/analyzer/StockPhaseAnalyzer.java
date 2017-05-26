package henry.analyzer;

import henry.analyzer.PhaseReport.PhaseType;
import henry.commons.CalendarUtil;
import henry.persistent.BasicDailyQuote;
import henry.persistent.BasicDailyQuoteService;
import henry.persistent.EMProvider;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class StockPhaseAnalyzer
{
    final String stock;
    int maxDuration;
    private Date endDate;
    private BasicDailyQuoteService quoteSrv = new BasicDailyQuoteService(EMProvider.getEM());
    private final static Logger logger = LogManager.getLogger();

    public StockPhaseAnalyzer(String stock, Date end, int max)
    {
        this.stock = stock;
        maxDuration = max;
        endDate = end;
    }

    public PhaseReport analyze()
    {
        PhaseReport report = new PhaseReport();
        // get 10 days statistics first
        List<BasicDailyQuote> quotes = getStatistics(report);
        if (quotes == null || quotes.size() < 1) {
            return null; // empty
        }
        // start analyzing: phase dividing
        {
            return analyzePhase(quotes, true);
        }
    }

    /**
     * assuming the quotes are ordered by date asc
     * 
     * @param quotes
     * @return
     */
    private PhaseReport analyzePhase(List<BasicDailyQuote> quotes, boolean isLeft)
    {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        Multimap<Double, Integer> priceToQuote = ArrayListMultimap.create(quotes.size(), 1);
        {
            int i = 0;
            for (BasicDailyQuote q : quotes) {
                stats.addValue(q.getClosing());
                priceToQuote.put(q.getClosing(), i);
                i++;
            }
        }
        double maxPrice = stats.getMax();
        double minPrice = stats.getMin();
        if ((maxPrice - minPrice) / minPrice < 0.03) { // enter stable phase
            PhaseReport report = new PhaseReport();
            report.setType(PhaseType.STABLE);
            closeReport(report, quotes, 0, quotes.size() - 1);
            return report;
        }
        // get the quote with greatest closing price.
        Collection<Integer> i_quote = priceToQuote.get(maxPrice);
        int i_max = i_quote.iterator().next();
        // BasicDailyQuote maxQuote = quotes.get(i_max);
        int start = i_max + 1;
        PhaseReport right_report = null;
        if (i_max != quotes.size() - 1) { // the last day is the max point then let it in rise phase
            right_report = new PhaseReport();
            // look forward; expecting a drop phase
            for (int i = i_max + 1; i < quotes.size(); i++) {
                BasicDailyQuote q = quotes.get(i);
                double gap = q.getGap();
                if (gap <= 0.01) { // still in drop phase
                    if (i == quotes.size() - 1) {
                        right_report.setType(PhaseType.SLOW_DROP);
                        this.closeReport(right_report, quotes, start, i);
                        break;
                    }
                    else {
                        continue;
                    }
                }
                else {
                    // close drop phase report
                    right_report.setType(PhaseType.SLOW_DROP);
                    this.closeReport(right_report, quotes, start, i - 1);
                    // if (i != quotes.size() - 1) {
                    // enter rise phase
                    List<BasicDailyQuote> subQuotes = quotes.subList(i, quotes.size());
                    PhaseReport nextPage = analyzePhase(subQuotes, true);
                    if (nextPage != null)
                        nextPage.setLastPhase(right_report);
                    // }
                    break;
                }
            }
        }

        // look backward: expecting a rise phase
        int end = i_max;
        PhaseReport left_report = new PhaseReport();
        for (int i = i_max; i >= 0; i--) {
            BasicDailyQuote q = quotes.get(i);
            double gap = q.getGap();
            if (gap >= 0 || Math.abs(gap / q.getClosing()) < 0.01) { // still in rise
                if (i == 0) {
                    left_report.setType(PhaseType.SLOW_RISE);
                    this.closeReport(left_report, quotes, i, end);
                    break;
                }
                else {
                    continue;
                }
            }
            else {
                // close rise phase report
                left_report.setType(PhaseType.SLOW_RISE);
                this.closeReport(left_report, quotes, i + 1, end);
                // if (i != 0) {
                // enter drop phase
                List<BasicDailyQuote> subQuotes = quotes.subList(0, i + 1);
                left_report.setLastPhase(analyzePhase(subQuotes, false));
                // }
                break;
            }
        }
        if (right_report != null) {
            right_report.setLastPhase(left_report);
        }
        if (isLeft && left_report != null) {
            return left_report.getMostLeftPhase();
        }
        else {
            return right_report.getMostRightPhase();
        }
    }

    private void closeReport(PhaseReport report, List<BasicDailyQuote> quotes, int start, int end)
    {
        report.setStart(quotes.get(start).getDate());
        report.setEnd(quotes.get(end).getDate());
        for (int i = start; i <= end; i++) {
            report.addQuote(quotes.get(i));
        }
        report.setDuration(end - start + 1);
        logger.info("determined a phase: {}", report);
    }

    private List<BasicDailyQuote> getStatistics(PhaseReport report)
    {
        // Calendar today = CalendarUtil.getToday();
        Date end = endDate;
        Date start = CalendarUtil.getAfterDays(CalendarUtil.getCalendarByMilliSecond(endDate.getTime()), -maxDuration);
        List<BasicDailyQuote> quotes = quoteSrv.getBasicDailyQuotes(stock, start, end); // order
                                                                                        // by
                                                                                        // date
                                                                                        // desc
                                                                                        // if (quotes.size() < maxDuration) {
        // quotes.addAll(quoteSrv.getBasicDailyQuotes(stock, end, CalendarUtil.getAfterDays(today, -7)));
        // }
        return quotes;

    }

    public static PhaseReport getLatestReport(String stock, Date end, int duration)
    {
        logger.info("stock: {}", stock);
        StockPhaseAnalyzer pa = new StockPhaseAnalyzer(stock, end, duration);
        PhaseReport p = pa.analyze();
        if (p == null)
            return null;
        PhaseReport latest = p.getMostRightPhase();
        return latest;
    }
}
