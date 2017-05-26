package henry.persistent;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DayPriceVolTest
{
	private static final String TEST_STOCK_ID = "testStock";
	private final static EntityManager em = EMProvider.getEMTest();
    static DayPriceVolService service = new DayPriceVolService(em);
    
    @BeforeClass
    public static void setup()
    {
    	service.deleteByStock(Arrays.asList(TEST_STOCK_ID));
    }
    
    @Test
    public void testCreate()
    {
        
        Date date = new Date();
        em.getTransaction().begin();
        service.createRecord(date, TEST_STOCK_ID, 158, 50000, 12);
        service.createRecord(date, TEST_STOCK_ID, 159, 50000, 13);
        em.getTransaction().commit();
        List<DayPriceVol> dv = service.selectByStockDate(TEST_STOCK_ID, date, date);
        Assert.assertEquals(dv.size(), 2);
        DayPriceVol d1 = dv.get(0);
        Assert.assertEquals(d1.getPrice(), 158);
        Assert.assertEquals(d1.getPercentage(), 12);
        DayPriceVol d2 = dv.get(1);
        Assert.assertEquals(d2.getPrice(), 159);
        Assert.assertEquals(d2.getPercentage(), 13);
    }
    
    @Test (dependsOnMethods="testCreate")
    public void testDistinct()
    {
        
        List<String> endStocks = service.distinctStocksFilteredByDate(new Date());
        Assert.assertEquals(endStocks.size(), 1);
        
    }
    
    @AfterClass
    public static void tearDown()
    {
    	service.deleteByStock(Arrays.asList(TEST_STOCK_ID));
    }

}
