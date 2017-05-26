package dailySelect;

import henry.commons.CalendarUtil;
import henry.persistent.BasicDailyQuote;
import henry.persistent.BasicDailyQuoteService;
import henry.persistent.EMProvider;
import henry.scoring.GoldVolScoring;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import dailyRun.Stocks;

public class StockScoring
{

    public static void main(String[] args)
    {
        EntityManager em = EMProvider.getEM();
        BasicDailyQuoteService svr = new BasicDailyQuoteService(em);

        double score = 0.0;
        List<String> stocks = Stocks.getAllStocksNoPrefix();
        Date start = CalendarUtil.getDate(2014, 11, 3);
        Date end = CalendarUtil.getDate(2014, 11, 13);
        for (String stock : stocks) {
            List<BasicDailyQuote> quotes = svr.getBasicDailyQuotes(stock, start, end);

            int size = quotes.size();
            for (int i = 0; i < size - 4; i++) {
                List<BasicDailyQuote> sub4 = quotes.subList(i, i + 4);
                GoldVolScoring s = new GoldVolScoring(sub4);
                if (s.score() > 0) {
                    record(stock, sub4);
                    break;
                }
                i++;
            }
        }

    }

    private static void record(String stock, List<BasicDailyQuote> sub4)
    {
        System.out.println(stock);
    }

}
