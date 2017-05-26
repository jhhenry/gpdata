package henry.analyzer;

import henry.persistent.DayPriceVol;
import henry.persistent.DayPriceVolService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class DailyStatisticAnalyer
{
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("DayPriceVolService");
    static EntityManager em = emf.createEntityManager();
    static DayPriceVolService service = new DayPriceVolService(em);

    int minCount = 3;

    public void generateStatistics(String stock, Date start, Date end, FileWriter w) throws IOException
    {
        List<DayPriceVol> rowSet = new DayPriceVolService(em).selectByStockDate(stock, start, end);

        DescriptiveStatistics stats = new DescriptiveStatistics();

        Map<Integer, Integer> pricePer = aggregatePercentageByPrice(rowSet);

        if (pricePer.size() > minCount) {
            for (Integer v : pricePer.keySet()) {
                stats.addValue(v);
            }

            double priceMean = Math.floor(stats.getMean() / 10) / 10.0;
            double gap = (stats.getMax() - stats.getMin()) / stats.getMin();
            stats.clear();

            for (Integer v : pricePer.values()) {
                stats.addValue(v);
            }
            double dev = stats.getStandardDeviation();

            double skewness = stats.getSkewness();
            double variance = stats.getPopulationVariance();
            ReportFields fields = new ReportFields();
            fields.setStock(stock).setPriceMean(priceMean).setDev(dev).setVariance(variance).setSkewness(skewness).setGap(gap);

            if (gap < 0.2) {
                report(fields, w);
            }

        }

        // for (rowSet)

    }

    public void calculateVolumnBelow(String stock, double separatrix, Date start, Date end, FileWriter w)
    {
        List<DayPriceVol> rowSet = new DayPriceVolService(em).selectByStockDate(stock, start, end);
        Map<Integer, Integer> pricePer = aggregatePercentageByPrice(rowSet);

    }

    private static class ReportFields
    {
        private String stock;
        private double dev;
        private double skewness;
        private double variance;
        private double gap;
        private double priceMean;

        public String toString()
        {
            StringBuilder sb = new StringBuilder(100);
            sb.append(stock).append(",").append(dev).append(",").append(dev / priceMean).append(",").append(priceMean).append(",").append(skewness).append(",").append(variance).append(",")
                    .append(gap).append("\r\n");
            return sb.toString();
        }

        public String getStock()
        {
            return stock;
        }

        public ReportFields setStock(String stock)
        {
            this.stock = stock;
            return this;
        }

        public double getDev()
        {
            return dev;
        }

        public ReportFields setDev(double dev)
        {
            this.dev = dev;
            return this;
        }

        public double getPriceMean()
        {
            return priceMean;
        }

        public ReportFields setPriceMean(double priceMean)
        {
            this.priceMean = priceMean;
            return this;
        }

        public double getSkewness()
        {
            return skewness;
        }

        public ReportFields setSkewness(double skewness)
        {
            this.skewness = skewness;
            return this;
        }

        public double getVariance()
        {
            return variance;
        }

        public ReportFields setVariance(double variance)
        {
            this.variance = variance;
            return this;
        }

        public double getGap()
        {
            return gap;
        }

        public ReportFields setGap(double gap)
        {
            this.gap = gap;
            return this;
        }
    }

    void report(ReportFields fields, FileWriter w) throws IOException
    {
        w.write(fields.toString());
    }

    Map<Integer, Integer> aggregatePercentageByPrice(List<DayPriceVol> rowSet)
    {
        if (rowSet == null || rowSet.size() < 10)
            ;
        Map<Integer, Integer> data = new HashMap<>(20);

        Map<Integer, Long> priceVols = new HashMap<>();
        Long totalVol = new Long(0);
        for (DayPriceVol row : rowSet) {
            int p = row.getPrice();
            Long vol = priceVols.get(p);
            totalVol += row.getVolumn();
            if (vol == null) {
                priceVols.put(p, row.getVolumn());
            }
            else {
                priceVols.put(p, vol + row.getVolumn());
            }
        }
        Double totalVolD = totalVol.doubleValue();
        for (Integer price : priceVols.keySet()) {
            Double per = Math.rint((priceVols.get(price).doubleValue() / totalVolD) * 10000.00);
            data.put(price, per.intValue());
        }

        return data;
    }

    public static void main(String[] args)
    {
        double d = 1151.645613;
        System.out.println();
    }
}
