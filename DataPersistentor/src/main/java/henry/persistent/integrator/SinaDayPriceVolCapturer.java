package henry.persistent.integrator;

import henry.html.extractor.internal.TableCell;
import henry.html.extractor.internal.TableCells;
import henry.html.extractor.internal.TableRowVisitor;
import henry.persistent.DayPriceVolService;
import henry.persistent.EMProvider;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.htmlparser.Tag;

public class SinaDayPriceVolCapturer extends TableRowVisitor
{
    private static final Logger logger = LogManager.getLogger();
    EntityManager em = EMProvider.getEM();

    private Date date;
    private String stock;
    private DayPriceVolService service;

    public SinaDayPriceVolCapturer(Date d, String stockId)
    {
        stock = stockId;
        date = d;
        service = new DayPriceVolService(em);
    }

    @Override
    protected void onRow(TableCells row, boolean isHeadRow)
    {
        if (!isHeadRow) {
            List<TableCell> cells = row.getCells();
            if (cells != null && cells.size() >= 3) {

                int price = Integer.valueOf(cells.get(0).getText().replace(".", ""));
                long volumn = Long.valueOf(cells.get(1).getText());
                String p = cells.get(2).getText();
                double p2 = Double.valueOf(p.substring(0, p.length() - 1)) * 100;
                int percentage = Double.valueOf(p2).intValue();
                logger.info("add price vol of {} on {}", stock, date);
                service.createRecord(date, stock, price, volumn, percentage);

            }
        }

    }

    @Override
    protected void onTableEnd()
    {
        em.getTransaction().commit();
    }

    @Override
    protected void onTableStart(Tag tag)
    {
        em.getTransaction().begin();

    }

}
