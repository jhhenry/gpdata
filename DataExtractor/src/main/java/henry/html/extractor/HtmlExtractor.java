package henry.html.extractor;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.visitors.NodeVisitor;

public class HtmlExtractor
{
    public void extract(InputStream is1, final IHtmlNodeVisitor v) throws Exception
    {
        InputStream is = new BufferedInputStream(is1);
        Page htmlPage = new Page(is, "utf-8");
        Lexer l = new Lexer(htmlPage);

        Node node = null;
        while ((node = l.nextNode()) != null)
        {
            node.accept(new NodeVisitor()
            {
                public void visitTag(Tag tag)
                {
                    v.visitTag(tag);
                }

                public void visitStringNode(Text string)
                {
                    v.visitStringNode(string);
                }

                public void visitEndTag(Tag tag)
                {
                    v.visitEndTag(tag);
                }
            });
        }
    }
}
