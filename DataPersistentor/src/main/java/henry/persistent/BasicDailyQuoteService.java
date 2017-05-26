package henry.persistent;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

public class BasicDailyQuoteService
{
    private EntityManager em;

    public BasicDailyQuoteService(EntityManager em)
    {
        this.em = em;
    }

    /**
     * @param date
     * @param stock
     * @param opening
     * @param closing
     * @param gap
     * @param lowest
     * @param highest
     * @param vol
     * @param totalDealPrice
     * @param turnover
     */
    public void createRecord(Date date, String stock, double opening, double closing, double gap, // compared
                                                                                                  // to
                                                                                                  // yesterday's
                                                                                                  // closing
            double lowest, double highest, long vol, // unit: shou
            double totalDealPrice,// unit: wan yuan
            double turnover)
    {

        BasicDailyQuote r = new BasicDailyQuote();
        r.setDate(date);
        r.setStock(stock);
        r.setOpening(opening);
        r.setClosing(closing);
        r.setGap(gap);
        r.setLowest(lowest);
        r.setHighest(highest);
        r.setVol(vol);
        r.setTotalDealPrice(totalDealPrice);
        r.setTurnover(turnover);
        em.persist(r);
    }

    public List<BasicDailyQuote> getBasicDailyQuotes(String stock, Date start, Date end)
    {
        List<BasicDailyQuote> r = em.createNamedQuery("BasicDailyQuote.findByStockAndDate", BasicDailyQuote.class).setParameter("stock", stock).setParameter("start", start).setParameter("end", end)
                .getResultList();
        return r;
    }

    public List<BasicDailyQuote> getOneDayQuotes(Date date)
    {
        List<BasicDailyQuote> r = em.createNamedQuery("BasicDailyQuote.findOneDayQuote", BasicDailyQuote.class).setParameter("date", date).getResultList();
        return r;
    }

}
