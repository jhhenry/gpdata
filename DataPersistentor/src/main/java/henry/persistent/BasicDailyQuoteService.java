package henry.persistent;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import henry.commons.CalendarUtil;
import henry.commons.DateUtils;

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
    
    public void createRecord(BasicDailyQuote r)
    {
    	em.persist(r);
    }
    
    /**
     * Unlike other methods, it commits the changes to the db.
     * If you don't want it, please use the "create" method.
     * @param qs
     */
    public void persist(List<BasicDailyQuote> qs)
    {
    	em.getTransaction().begin();
		for (BasicDailyQuote q : qs) {
			this.createRecord(q);
		}
		em.getTransaction().commit();
    }
    

    public List<BasicDailyQuote> getBasicDailyQuotes(String stock, Date start, Date end)
    {
        List<BasicDailyQuote> r = em.createNamedQuery("BasicDailyQuote.findByStockAndDate", BasicDailyQuote.class).setParameter("stock", stock).setParameter("start", start).setParameter("end", end)
                .getResultList();
        return r;
    }
    
    public List<BasicDailyQuote> getAllBasicDailyQuotes(String stock)
    {
        List<BasicDailyQuote> r = em.createNamedQuery("BasicDailyQuote.findAllByStock", BasicDailyQuote.class).setParameter("stock", stock)
                .getResultList();
        return r;
    }

    public List<BasicDailyQuote> getOneDayQuotes(Date date)
    {
        List<BasicDailyQuote> r = em.createNamedQuery("BasicDailyQuote.findOneDayQuote", BasicDailyQuote.class).setParameter("date", date).getResultList();
        return r;
    }
    
    public BasicDailyQuote getLatestQuote(String stock)
    {
    	CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
    	CriteriaQuery<BasicDailyQuote> qdef = queryBuilder.createQuery(BasicDailyQuote.class);
    	Root<BasicDailyQuote> root = qdef.from(BasicDailyQuote.class);
    	qdef.where(queryBuilder.equal(root.get("stock"), stock)).orderBy(queryBuilder.desc(root.get("startDate")));
    	TypedQuery<BasicDailyQuote> createQuery = em.createQuery(qdef).setMaxResults(1);
    	List<BasicDailyQuote> r = createQuery.getResultList();
//        List<BasicDailyQuote> r = ?em.createNamedQuery("BasicDailyQuote.findLatestQuote", BasicDailyQuote.class).setParameter("stock", stock).getResultList();
        return r!= null && r.size() > 0 ? r.get(0) : null;
    }
    
    public void delete(String stock, Date start, Date end)
    {
    	if (start == null && end != null) {
    		start = end;
    	} else if (start != null && end == null) {
    		end = start;
    	} else if (start == null && end == null) {
    		start = CalendarUtil.getDate(1991, 1, 1);
    		end = DateUtils.asDate(LocalDate.now().plusYears(15));
    	}
        em.createNamedQuery("BasicDailyQuote.deleteQuotesByStockAndDate", BasicDailyQuote.class).setParameter("stock", stock).setParameter("start", start).setParameter("end", end).executeUpdate();
    }
    
    public void deleteAll(String stock)
    {
    	delete(stock, null, null);
    }

}
