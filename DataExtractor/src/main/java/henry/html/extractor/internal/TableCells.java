package henry.html.extractor.internal;

import java.util.ArrayList;
import java.util.List;

public final class TableCells
{
    private List<TableCell> cells = new ArrayList<TableCell>();

    public List<TableCell> getCells()
    {
        return cells;
    }
    
    public void addCell(TableCell cell)
    {
        cells.add(cell);
    }

}
