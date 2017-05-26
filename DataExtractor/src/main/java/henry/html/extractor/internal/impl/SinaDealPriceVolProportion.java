package henry.html.extractor.internal.impl;

import henry.html.extractor.internal.TableCell;
import henry.html.extractor.internal.TableCells;
import henry.html.extractor.internal.TableRowVisitor;

import java.util.List;

import org.htmlparser.Tag;

public class SinaDealPriceVolProportion extends TableRowVisitor
{

    private double less;
    private double greater;
    private double all;
    @Override
    protected void onRow(TableCells row, boolean isHeadRow)
    {
        
        if (!isHeadRow) {
            SinaDealPriceVol data = new SinaDealPriceVol();
            List<TableCell> cells = row.getCells();
            data.setPrice(Double.valueOf(cells.get(0).getText()));
            data.setVol(Integer.valueOf(cells.get(1).getText()));
            String p = cells.get(2).getText();
            double p2 = Double.valueOf(p.substring(0, p.length() - 1));
            data.setPercentage(p2);
            all += p2;
            if (data.getPrice() < 8.40 ) {
                less += data.getPercentage();
                
            }
            if (data.getPrice() > 8.80 ) {
                greater += data.getPercentage();
                
            }
            
        }
    }
    
    protected void onTableEnd()
    {
    }
    
    private static class SinaDealPriceVol
    {
        public double getPrice()
        {
            return price;
        }
        public void setPrice(double price)
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
        public double getPercentage()
        {
            return percentage;
        }
        public void setPercentage(double percentage)
        {
            this.percentage = percentage;
        }
        private double price;
        private long vol; //unit: gu
        private double percentage;
    }

    @Override
    protected void onTableStart(Tag tag)
    {
        // TODO Auto-generated method stub
        
    }


}
