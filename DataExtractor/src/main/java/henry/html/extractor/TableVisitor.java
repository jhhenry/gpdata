package henry.html.extractor;

import java.util.ArrayDeque;
import java.util.Deque;

import org.htmlparser.Tag;
import org.htmlparser.Text;

public abstract class TableVisitor implements IHtmlNodeVisitor
{
    private Tag currentTag;
    private Deque<Tag> tagStack = new ArrayDeque<Tag>(4);
    private boolean isHeadRow;
    @Override
    public void visitTag(Tag tag)
    {
        if (tag == null) return;
        currentTag = tag;
        String tagName = tag.getTagName();
        if ("THEAD".equals(tagName)) {
            isHeadRow = true;
            onHeader(tag);
        } else if ("TH".equals(tagName)) {
            onHeader(tag);
        } else if ("TR".equals(tagName)) {
            onRow(tag);
        } else if ("TD".equals(tagName)) {
            onCell(tag);
        } else if ("TABLE".equals(tagName)) {
            onTableStart(tag);
        } else {
            return;
        }
        tagStack.push(tag);
        
        

    }

    @Override
    public void visitStringNode(Text string)
    {
        currentTag = tagStack.peekFirst();
        if (currentTag == null) return;
        String tagName = currentTag.getTagName();
        if ("TH".equals(tagName)) {
            onCellText(currentTag, string);
        } else if ("TR".equals(tagName)) {
            onRowText(currentTag, string);
        } else if ("TD".equals(currentTag.getTagName())) {
            onCellText(currentTag, string);
        }
        

    }
    
    public void visitEndTag(Tag tag)
    {
        
        if (tag == null) return;
        
        String tagName = tag.getTagName();
        if ("THEAD".equals(tagName)) {
            isHeadRow = false;
            onHeaderEnd(tag);
        } else if ("TH".equals(tagName)) {
            onCellEnd(tag);
        } else if ("TR".equals(tagName)) {
            onRowEnd(tag, isHeadRow);
        } else if ("TD".equals(tagName)) {
            onCellEnd(tag);
        } else if ("TABLE".equals(tagName)) {
            onTableEnd();
        } else {
            return;
        }
        tagStack.pop();
    }
    
    abstract protected void onCellEnd(Tag tag);
    abstract protected void onCell(Tag tag);
    abstract protected  void onRow(Tag tag);

    abstract protected void onHeader(Tag tag); 
    
    abstract protected  void onRowEnd(Tag tag, boolean isHeadRow2);

    abstract protected void onHeaderEnd(Tag tag); 
    
    abstract protected void onCellText(Tag tag, Text text);
    abstract protected  void onRowText(Tag tag, Text text);

    abstract protected void onHeaderText(Tag tag, Text text); 
    
    abstract protected void onTableStart(Tag tag);
    abstract protected void onTableEnd();

}
