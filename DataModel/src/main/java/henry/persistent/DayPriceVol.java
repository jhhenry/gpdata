package henry.persistent;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: DayPriceVol
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = "DayPriceVol.findByDate", query = "select o from DayPriceVol o where o.date = :date") , 
 @NamedQuery(name = "DayPriceVol.distinctAll", query = "select distinct(o.stock) from DayPriceVol o"),//,
@NamedQuery(name = "DayPriceVol.distinctByDate", query = "select distinct(o.stock) from DayPriceVol o where o.date = :date") ,
@NamedQuery(name = "DayPriceVol.selectByStockDate", query = "select o from DayPriceVol o where o.stock = :stock and o.date between :startDate and :endDate order by o.date")  
})
public class DayPriceVol implements Serializable {

	   
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String stock;
	private int price;
	private int percentage;
	private long volumn;
	@Temporal(value = TemporalType.DATE)
	private Date date;
	private static final long serialVersionUID = 1L;

	public DayPriceVol() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public String getStock() {
		return this.stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}   
	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}   
	public int getPercentage() {
		return this.percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}   
	public long getVolumn() {
		return this.volumn;
	}

	public void setVolumn(long volumn) {
		this.volumn = volumn;
	}
    public void setDate(Date date2)
    {
        this.date = date2;
        
    }
    
    public Date getDate()
    {
        return date;
    }
   
}
