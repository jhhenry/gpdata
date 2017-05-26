package henry.persistent;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "BasicWeeklyQuote.findByStockAndDate", 
			query = "select o from BasicWeeklyQuote o where o.stock = :stock and ((o.startDate <= :start and o.endDate >= :start) or (o.startDate >= :start and o.endDate<= :end) or (o.startDate <= :end and o.endDate >= :end)) order by o.endDate"),
	@NamedQuery(name = "BasicWeeklyQuote.deleteQuotesByStockAndDate",
			query = "delete from BasicWeeklyQuote o where o.stock = :stock and ((o.startDate <= :start and o.endDate >= :start) or (o.startDate >= :start and o.endDate<= :end) or (o.startDate <= :end and o.endDate >= :end))"),
    @NamedQuery(name = "BasicWeeklyQuote.findOneWeekQuote", 
        	query = "select o from BasicWeeklyQuote o where o.startDate <= :date and o.endDate >= :date") })
public class BasicWeeklyQuote extends AbstractCandle
{
	@Temporal(value = TemporalType.DATE)
	private Date endDate;
	public void setEndDate(Date date)
	{
		this.endDate = date;
	}

	public Date getEndDate()
	{
		return endDate;
	}

}
