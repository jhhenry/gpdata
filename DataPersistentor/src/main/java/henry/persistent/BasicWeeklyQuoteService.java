package henry.persistent;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import henry.commons.CalendarUtil;
import henry.commons.DateUtils;

public class BasicWeeklyQuoteService
{
    private EntityManager em;

    public BasicWeeklyQuoteService(EntityManager em)
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
    public void createRecord(Date startdate, Date endDate, String stock, double opening, double closing, double gap, double gapPercentage,
            double lowest, double highest, long vol, // unit: shou
            double totalDealPrice,// unit: wan yuan
            double turnover)
    {

        BasicWeeklyQuote r = new BasicWeeklyQuote();
        r.setStartDate(startdate);
        r.setEndDate(endDate);
        r.setStock(stock);
        r.setOpening(opening);
        r.setClosing(closing);
        r.setGap(gap);
        r.setGapPercentage(gapPercentage);
        r.setLowest(lowest);
        r.setHighest(highest);
        r.setVol(vol);
        r.setTotalDealPrice(totalDealPrice);
        r.setTurnover(turnover);
        em.persist(r);
    }
    
    public void create(BasicWeeklyQuote week)
    {
    	 em.persist(week);
    }
    

    public List<BasicWeeklyQuote> getBasicWeeklyQuotes(String stock, Date start, Date end)
    {
        List<BasicWeeklyQuote> r = em.createNamedQuery("BasicWeeklyQuote.findByStockAndDate", BasicWeeklyQuote.class).setParameter("stock", stock).setParameter("start", start).setParameter("end", end)
                .getResultList();
        return r;
    }
    
    public void deleteWeeklyQuotes(String stock, Date start, Date end)
    {
    	if (start == null && end != null) {
    		start = end;
    	} else if (start != null && end == null) {
    		end = start;
    	} else if (start == null && end == null) {
    		start = CalendarUtil.getDate(1970, 1, 1);
    		end = DateUtils.asDate(LocalDate.now().plusYears(15));
    	}
        em.createNamedQuery("BasicWeeklyQuote.deleteQuotesByStockAndDate", BasicWeeklyQuote.class).setParameter("stock", stock).setParameter("start", start).setParameter("end", end).executeUpdate();
    }

    public List<BasicWeeklyQuote> getOneWeekQuotes(Date date)
    {
        List<BasicWeeklyQuote> r = em.createNamedQuery("BasicWeeklyQuote.findOneWeekQuote", BasicWeeklyQuote.class).setParameter("date", date).getResultList();
        return r;
    }

}
