package henry.analyzer;

import henry.commons.CalendarUtil;
import henry.persistent.BasicDailyQuote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.stringtemplate.v4.ST;

public class PhaseReport
{
    public static enum PhaseType
    {
        STABLE, SLOW_DROP, SHARP_DROP, SLOW_RISE, SHARP_RISE, SHOCK
    }

    Date start;
    Date end;
    int duration;

    PhaseType type;
    PhaseReport lastPhase;
    private PhaseReport nextPhase;

    double total_turnover;
    double price_deviation;

    double meanPrice;

    double maxPrice;

    double lowestPrice;

    double modePrice;

    List<BasicDailyQuote> quotes = new ArrayList<>(5);

    public void addQuote(BasicDailyQuote q)
    {
        if (q != null) {
            quotes.add(q);
        }
    }

    public List<BasicDailyQuote> getQuotes()
    {
        return new ArrayList<>(quotes);
    }

    public PhaseType getType()
    {
        return type;
    }

    public PhaseReport setType(PhaseType type)
    {
        this.type = type;
        return this;
    }

    public PhaseReport getLastPhase()
    {
        return lastPhase;
    }

    public PhaseReport getMostLeftPhase()
    {
        PhaseReport l = this;
        while (l.getLastPhase() != null) {
            l = l.getLastPhase();
        }
        return l;
    }

    public PhaseReport getMostRightPhase()
    {
        PhaseReport l = this;
        while (l.getNextPhase() != null) {
            l = l.getNextPhase();
        }
        return l;
    }

    public PhaseReport setLastPhase(PhaseReport lastPhase)
    {
        this.lastPhase = lastPhase;
        if (lastPhase != null)
            lastPhase.nextPhase = (this);
        return this;
    }

    public double getTotal_turnover()
    {
        return total_turnover;
    }

    public PhaseReport setTotal_turnover(double total_turnover)
    {
        this.total_turnover = total_turnover;
        return this;
    }

    public double getDeviation()
    {
        return price_deviation;
    }

    public PhaseReport setDeviation(double deviation)
    {
        this.price_deviation = deviation;
        return this;
    }

    public double getMeanPrice()
    {
        return meanPrice;
    }

    public PhaseReport setMeanPrice(double meanPrice)
    {
        this.meanPrice = meanPrice;
        return this;
    }

    public double getMaxPrice()
    {
        return maxPrice;
    }

    public PhaseReport setMaxPrice(double maxPrice)
    {
        this.maxPrice = maxPrice;
        return this;
    }

    public double getLowestPrice()
    {
        return lowestPrice;
    }

    public PhaseReport setLowestPrice(double lowestPrice)
    {
        this.lowestPrice = lowestPrice;
        return this;
    }

    public double getModePrice()
    {
        return modePrice;
    }

    public PhaseReport setModePrice(double modePrice)
    {
        this.modePrice = modePrice;
        return this;
    }

    public Date getStart()
    {
        return start;
    }

    public PhaseReport setStart(Date start)
    {
        this.start = start;
        return this;
    }

    public Date getEnd()
    {
        return end;
    }

    public PhaseReport setEnd(Date end)
    {
        this.end = end;
        return this;
    }

    public int getDuration()
    {
        return duration;
    }

    public PhaseReport setDuration(int duration)
    {
        this.duration = duration;
        return this;
    }

    public PhaseReport getNextPhase()
    {
        return nextPhase;
    }

    public String toString()
    {
        ST st = new ST("start: <start>, end : <end>, phase: <phase>");
        if (start != null)
            st.add("start", CalendarUtil.getDashDate().format(start));
        if (end != null)
            st.add("end", CalendarUtil.getDashDate().format(end));
        if (type != null)
            st.add("phase", this.type.name());
        return st.render();
    }

}
