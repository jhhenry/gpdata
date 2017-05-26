package henry.persistent;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
@IdClass(CandleId.class)
abstract public class AbstractCandle
{
	@Id
	protected String stock;
	@Id
    @Temporal(value = TemporalType.DATE)
    protected Date startDate;
	
	private double opening;
	private double closing;
	private double gap; // compared to last week's closing
	private double gapPercentage; // compared to last week's closing
	private double lowest;
	private double highest;
	private long vol; // unit: shou
	private double totalDealPrice;// unit: wan yuan
	private double turnover;

	public double getOpening()
	{
		return opening;
	}

	public void setOpening(double opening)
	{
		this.opening = opening;
	}

	public double getClosing()
	{
		return closing;
	}

	public void setClosing(double closing)
	{
		this.closing = closing;
	}

	public double getGap()
	{
		return gap;
	}

	public void setGap(double gap)
	{
		this.gap = gap;
	}
	
	public double getGapPercentage()
	{
		return gapPercentage;
	}

	public void setGapPercentage(double gapPercentage)
	{
		this.gapPercentage = gapPercentage;
	}

	public double getLowest()
	{
		return lowest;
	}

	public void setLowest(double lowest)
	{
		this.lowest = lowest;
	}

	public double getHighest()
	{
		return highest;
	}

	public void setHighest(double highest)
	{
		this.highest = highest;
	}

	public long getVol()
	{
		return vol;
	}

	public void setVol(long vol)
	{
		this.vol = vol;
	}

	public double getTotalDealPrice()
	{
		return totalDealPrice;
	}

	public void setTotalDealPrice(double totalDealPrice)
	{
		this.totalDealPrice = totalDealPrice;
	}

	public double getTurnover()
	{
		return turnover;
	}

	public void setTurnover(double turnover)
	{
		this.turnover = turnover;
	}

	public String getStock()
	{
		return stock;
	}

	public void setStock(String stock)
	{
		this.stock = stock ;;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date date)
	{
		this.startDate = date;
	}
	
	public String toString()
	{
		return stock + " date: " + startDate;
	}


}

class CandleId
{
	private String stock;
	private Date startDate;
	public String getStock()
	{
		return stock;
	}
	public void setStock(String stock)
	{
		this.stock = stock;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
}
