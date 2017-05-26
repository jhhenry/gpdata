package henry.html.extractor.internal;

import henry.html.extractor.TableVisitor;

import org.htmlparser.Tag;
import org.htmlparser.Text;

public abstract class TableRowVisitor extends TableVisitor
{
    private TableCells row;
    
    @Override
    protected void onCellEnd(Tag tag)
    {

    }

    @Override
    protected void onCell(Tag tag)
    {

    }

    @Override
    protected void onRow(Tag tag)
    {
         row = new TableCells();

    }

    @Override
    protected void onHeader(Tag tag)
    {

    }

    @Override
    protected void onRowEnd(Tag tag, boolean isHeadRow)
    {
        onRow(row, isHeadRow);
        row = null;
    }
    
    abstract protected void onRow(TableCells row, boolean isHeadRow);
    @Override
    protected void onHeaderEnd(Tag tag)
    {

    }

    @Override
    protected void onCellText(Tag tag, Text text)
    {
        if (row != null) {
            row.addCell(new TableCell(text.getText()));
        }
    }
    
    @Override
    protected void onRowText(Tag tag, Text text)
    {

    }

    @Override
    protected void onHeaderText(Tag tag, Text text)
    {

    }

}
