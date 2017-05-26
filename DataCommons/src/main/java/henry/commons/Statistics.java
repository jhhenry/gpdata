package henry.commons;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Test;

public class Statistics
{
static List<Integer> data1 = new ArrayList<Integer>();
    
    static List<Integer> data2 = new ArrayList<Integer>();
    
    static List<Integer> data3 = new ArrayList<Integer>();
    
    static List<Integer> data4 = new ArrayList<Integer>();
    
    static {
        data1.add(80);
        data1.add(18);
        data1.add(1);
        data1.add(1);
        
        data2.add(25);
        data2.add(25);
        data2.add(25);
        data2.add(25);
        
        data3.add(40);
        data3.add(30);
        data3.add(10);
        data3.add(5);
        data3.add(5);
        data3.add(5);
        data3.add(4);
        data3.add(1);
        
        data4.add(50);
        data4.add(20);
        data4.add(5);
        data4.add(5);
        data4.add(5);
        data4.add(4);
        data4.add(1);
        
    }
    
    @Test
    public void testDescriptiveStatics()
    {
        calDescriptive(data1);
        data1.add(10);
        data1.add(10);
        calDescriptive(data1);
        calDescriptive(data2);
        calDescriptive(data3);
        
        calDescriptive(data4);
        
    }

    private void calDescriptive(List<? extends Number> d)
    {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        
        for (Number v : d) {
            stats.addValue(v.doubleValue());
        }
        print(d);
        print("mean: " + stats.getMean());
        print("Percentile 80: " + stats.getPercentile(80));
        print("StandardDeviation : " + stats.getStandardDeviation());
        print("Variance : " + stats.getVariance());;
        print("");
        
    }
    
    private static void print(Object o)
    {
        System.out.println(o);
    }
}
