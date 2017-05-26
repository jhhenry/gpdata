package henry.persistent;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

abstract public class BasicQuoteService<T extends AbstractCandle>
{
    private EntityManager em;

    public BasicQuoteService(EntityManager em)
    {
        this.em = em;
    }
    
    public void create(T candle)
    {
    	 em.persist(candle);
    }
    

    abstract public List<T> getBasicQuotes(String stock, Date start, Date end);
    
    abstract public void deleteQuotes(String stock, Date start, Date end);

   

}
