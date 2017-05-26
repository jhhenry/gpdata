package henry.analyzer;

public class PriceVolStatistics
{
    private int price;
    private long vol;
    private int percentage;
    public long addVol(long v)
    {
        vol += v;
        return vol;
    }
    public int getPrice()
    {
        return price;
    }
    public void setPrice(int price)
    {
        this.price = price;
    }
    public long getVol()
    {
        return vol;
    }
    public void setVol(long vol)
    {
        this.vol = vol;
    }
    public int getPercentage()
    {
        return percentage;
    }
    public void setPercentage(int percentage)
    {
        this.percentage = percentage;
    }
}
