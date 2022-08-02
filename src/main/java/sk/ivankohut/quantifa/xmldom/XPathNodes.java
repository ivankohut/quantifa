package sk.ivankohut.quantifa.xmldom;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.scalar.Unchecked;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Iterator;

@RequiredArgsConstructor
public class XPathNodes implements Iterable<Node> {

    private static final XPathFactory FACTORY = XPathFactory.newInstance();

    private final Scalar<? extends Node> node;
    private final XPathExpression expression;

    // disabling buggy SonarQube rule reporting false positive here
    @SuppressWarnings("javabugs:S2190")
    public XPathNodes(Scalar<? extends Node> node, String expression) {
        this(node, compileExpression(expression));
    }

    public XPathNodes(Text xml, String expression) {
        this(new XmlDocument(xml), expression);
    }

    private static XPathExpression compileExpression(String expression) {
        try {
            return FACTORY.newXPath().compile(expression);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Iterator<Node> iterator() {
        try {
            return new ListOfNodesIterator((NodeList) expression.evaluate(new Unchecked<>(node).value(), XPathConstants.NODESET));
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
