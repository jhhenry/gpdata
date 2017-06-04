package henry.persistent;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@NamedQueries({
		@NamedQuery(name = "BasicDailyQuote.findByStockAndDate", query = "select o from BasicDailyQuote o where o.stock = :stock and (o.startDate between :start and :end) order by o.startDate"),
		@NamedQuery(name = "BasicDailyQuote.findOneDayQuote", query = "select o from BasicDailyQuote o where o.startDate = :date"),
		@NamedQuery(name = "BasicDailyQuote.deleteQuotesByStockAndDate",
		query = "delete from BasicDailyQuote o where o.stock = :stock and o.startDate <= :end and o.startDate >= :start"),//,@NamedQuery(name = "BasicDailyQuote.findLatestQuote", query = "select o, o.startDate from BasicDailyQuote o where o.stock = :stock ORDER BY o.startDate limit 1")
		})
@Access(AccessType.PROPERTY)
public class BasicDailyQuote extends AbstractCandle
{
	@Id
    @Temporal(value = TemporalType.DATE)
	@Column(name = "date")
	public Date getStartDate()
	{
		return startDate;
	}

	@Transient
	public Date getDate()
	{
		return super.getStartDate();
	}

	public void setDate(Date date)
	{
		super.setStartDate(date);
		
	}
	
	public double getGapPercentage()
	{
		double gap = getGap();
		double closing  = getClosing();
		return closing != gap ? gap / (closing - gap) : 0d;
	}

}
