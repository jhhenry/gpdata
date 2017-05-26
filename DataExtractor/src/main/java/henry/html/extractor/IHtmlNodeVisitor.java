package henry.html.extractor;
import org.htmlparser.Tag;
import org.htmlparser.Text;


public interface IHtmlNodeVisitor
{
    public void visitTag(Tag tag);

    public void visitStringNode(Text string);

    public void visitEndTag(Tag tag);
}
