package henry.query;

import henry.analyzer.PhaseReport;
import henry.commons.CalendarUtil;
import henry.persistent.BasicDailyQuote;
import henry.persistent.BasicDailyQuoteService;
import henry.persistent.EMProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SimpleStaticInfoFilter implements IStockFilter
{
    private Double scale; // unit: yi yuan
    private List<String> stocks;

    public SimpleStaticInfoFilter(List<String> stocks, Double scale)
    {
        super();
        this.scale = scale;
        this.stocks = Collections.unmodifiableList(stocks);
    }

    public boolean filter()
    {
        BasicDailyQuoteService srv = new BasicDailyQuoteService(EMProvider.getEM());
        Date start = CalendarUtil.getDate(2014, 10, 13);
        List<BasicDailyQuote> filtered = new ArrayList<BasicDailyQuote>();
        // for (String stock : stocks) {
        List<BasicDailyQuote> info = srv.getOneDayQuotes(start);
        if (info.size() > 0) {
            for (BasicDailyQuote q : info) {
                double value = (q.getTotalDealPrice() / q.getTurnover()) / 100;
                if (value < scale) {
                    filtered.add(q);
                }
            }
        }
        // }
        return false;
    }

    @Override
    public boolean filter(String stock, PhaseReport report)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
