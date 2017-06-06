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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(closing);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(gap);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(gapPercentage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(highest);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lowest);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(opening);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
		temp = Double.doubleToLongBits(totalDealPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(turnover);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (vol ^ (vol >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractCandle other = (AbstractCandle) obj;
		if (Double.doubleToLongBits(closing) != Double.doubleToLongBits(other.closing))
			return false;
		if (Double.doubleToLongBits(gap) != Double.doubleToLongBits(other.gap))
			return false;
		if (Double.doubleToLongBits(gapPercentage) != Double.doubleToLongBits(other.gapPercentage))
			return false;
		if (Double.doubleToLongBits(highest) != Double.doubleToLongBits(other.highest))
			return false;
		if (Double.doubleToLongBits(lowest) != Double.doubleToLongBits(other.lowest))
			return false;
		if (Double.doubleToLongBits(opening) != Double.doubleToLongBits(other.opening))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		if (Double.doubleToLongBits(totalDealPrice) != Double.doubleToLongBits(other.totalDealPrice))
			return false;
		if (Double.doubleToLongBits(turnover) != Double.doubleToLongBits(other.turnover))
			return false;
		if (vol != other.vol)
			return false;
		return true;
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
