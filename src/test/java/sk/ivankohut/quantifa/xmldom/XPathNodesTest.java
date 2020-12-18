package sk.ivankohut.quantifa.xmldom;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XPathNodesTest {

    @Test
    void nodesAsEvaluationOfGivenXPathExpressionAgainstXml() {
        // exercise
        var sut = new XPathNodes(() -> "<a><b><c></c><c></c></b></a>", "/a/b/*");
        // verify
        assertThat(sut).extracting(Node::getNodeName).containsExactly("c", "c");
    }

    @Test
    void nodesAsEvaluationOfGivenXPathExpressionAgainstGivenNonRootNode() {
        // exercise
        var sut = new XPathNodes(() -> XmlDocumentTest.create("<a><b><c></c><c></c></b></a>").value().getFirstChild().getFirstChild(), "c");
        // verify
        assertThat(sut).extracting(Node::getNodeName).containsExactly("c", "c");
    }

    @Test
    void exceptionForIllegalXPathExpression() {
        assertThatThrownBy(() -> new XPathNodes(() -> "<a></a>", "?"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(XPathExpressionException.class);
    }
}
