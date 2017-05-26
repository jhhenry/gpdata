package henry.scoring;

import henry.persistent.BasicDailyQuote;

import java.util.List;

public class GoldVolScoring implements IQuotesBasedScoring
{
    private List<BasicDailyQuote> quotes;

    public GoldVolScoring(List<BasicDailyQuote> q)
    {
        quotes = q;
    }

    @Override
    public double score()
    {
        BasicDailyQuote q0 = quotes.get(0);
        BasicDailyQuote q1 = quotes.get(1);
        BasicDailyQuote q2 = quotes.get(2);
        BasicDailyQuote q3 = quotes.get(3);
        boolean priceRise = comparePrice(q1, q0) && comparePrice(q2, q1) && comparePrice(q3, q2);
        boolean volReduce = compareVol(q2, q1) && compareVol(q3, q2);
        boolean yang = true;
        for (BasicDailyQuote q : quotes) {
            yang = yang & isYang(q);
            if (!yang) {
                break;
            }
        }
        // int i = 0;
        // for (BasicDailyQuote q : quotes) {
        // priceRise = q.getClosing() <= quotes.
        // i++;
        // }
        if (priceRise && volReduce && yang && q0.getGap() >= 0 && q0.getTurnover() >= q3.getTurnover()) {
            return 1;
        }
        return 0;
    }

    private boolean comparePrice(BasicDailyQuote q1, BasicDailyQuote q2)
    {
        return (q1.getClosing() >= q2.getClosing()) || (Math.abs(q1.getClosing() - q2.getClosing()) / q1.getClosing() < 0.008);
    }

    private boolean compareVol(BasicDailyQuote q1, BasicDailyQuote q2)
    {
        return q1.getTurnover() <= q2.getTurnover();
    }

    private boolean isYang(BasicDailyQuote q)
    {
        return q.getClosing() >= q.getOpening();
    }
}
