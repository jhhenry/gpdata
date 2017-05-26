package henry.html.extractor;

import henry.html.extractor.internal.impl.SinaDealPriceVolProportion;

import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class HtmlExtractorTest
{

    //@Test
    public void testFile() throws Exception
    {
        InputStream is = HtmlExtractorTest.class.getClassLoader().getResourceAsStream("sinaDeal.txt"); // $NON-NLS
        HtmlExtractor ext = new HtmlExtractor();
        ext.extract(is, new SinaDealPriceVolProportion());
    }
    
    @Test
    public void testURLInputStream() throws Exception
    {
        URL u = new URL("http://market.finance.sina.com.cn/pricehis.php?symbol=sh600651&startdate=2014-09-04&enddate=2014-09-16");
        
        InputStream is = u.openStream(); // $NON-NLS
        HtmlExtractor ext = new HtmlExtractor();
        ext.extract(is, new SinaDealPriceVolProportion());
        
        
    }
    
    @Test
    public void testURLInputStream2() throws Exception
    {
        URL u = new URL("http://market.finance.sina.com.cn/pricehis.php?symbol=sh600651&startdate=2014-09-04&enddate=2014-09-17");
        
        InputStream is = u.openStream(); // $NON-NLS
        HtmlExtractor ext = new HtmlExtractor();
        ext.extract(is, new SinaDealPriceVolProportion());
        
        
    }
    
    @Test
    public void testURLInputStream3() throws Exception
    {
        URL u = new URL("http://market.finance.sina.com.cn/pricehis.php?symbol=sh600651&startdate=2014-09-04&enddate=2014-09-18");
        
        InputStream is = u.openStream(); // $NON-NLS
        HtmlExtractor ext = new HtmlExtractor();
        ext.extract(is, new SinaDealPriceVolProportion());
        
        
    }
    
    @Test
    public void testURLInputStream4() throws Exception
    {
        URL u = new URL("http://market.finance.sina.com.cn/pricehis.php?symbol=sh600651&startdate=2014-09-04&enddate=2014-09-19");
        
        InputStream is = u.openStream(); // $NON-NLS
        HtmlExtractor ext = new HtmlExtractor();
        ext.extract(is, new SinaDealPriceVolProportion());
        
        
    }
    
    

}
