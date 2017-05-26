package henry.persistent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: DayPriceVol
 *
 */

public class DayPriceVolService
{

    private EntityManager em;
    
    public DayPriceVolService(EntityManager em) {
        this.em = em;
    }
    
    public void createRecord(Date date, String stock, int price, long volumn, int percentage)
    {
        DayPriceVol r = new DayPriceVol();
        r.setPercentage(percentage);
        r.setPrice(price);
        r.setVolumn(volumn);
        r.setDate(date);
        r.setStock(stock);
        em.persist(r);
    }
    
    public List<String> distinctStocksFilteredByDate(Date d)
    {
        List<String> r = em.createNamedQuery("DayPriceVol.distinctByDate", String.class).setParameter("date",d, TemporalType.DATE).getResultList();
        return r;
    }
    
    public List<String> distinctAllStocks()
    {
        List<String> r = em.createNamedQuery("DayPriceVol.distinctAll", String.class).getResultList();
        return r;
    }
    
    public List<DayPriceVol> selectByStockDate(String stock, Date start, Date end)
    {
        List<DayPriceVol> r = em.createNamedQuery("DayPriceVol.selectByStockDate", DayPriceVol.class)
                .setParameter("stock", stock).setParameter("startDate", start).setParameter("endDate", end).getResultList();
        return r;
    }
    
    public void deleteByStock(List<String> stocks)
    {
        em.getTransaction().begin();
        for (String s : stocks) {
            em.createQuery("delete from DayPriceVol c where c.stock = :stock").setParameter("stock", s).executeUpdate();
        }
        em.getTransaction().commit();
    }
    
    public List<String> getMissingStocks(Date date)
    {
        
        List<String> endStocks = distinctStocksFilteredByDate(date);
        
        List<String> allStocks = distinctAllStocks();
        
        List<String> missingStocks = new ArrayList<String>();
        
        for (String s : allStocks) {
            if (!endStocks.contains(s)) {
                missingStocks.add(s);
            }
        }
        
       return missingStocks;
    }
}
