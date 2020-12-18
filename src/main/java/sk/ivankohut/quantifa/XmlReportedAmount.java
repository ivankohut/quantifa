package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.cactoos.iterable.Filtered;
import org.cactoos.scalar.Unchecked;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.xmldom.ChildNodes;
import sk.ivankohut.quantifa.xmldom.FirstFilteredNode;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class XmlReportedAmount implements ReportedAmount {

    private final Scalar<Node> date;
    private final Scalar<Node> value;

    public XmlReportedAmount(Node node, String lineItem) {
        var childNodes = new ChildNodes(node);
        this.date = new FirstFilteredNode(
                "StatementDate",
                new ChildNodes(new FirstFilteredNode("FPHeader", childNodes))
        );
        this.value = new FirstFilteredNode(
                n -> lineItem.equals(n.getAttributes().getNamedItem("coaCode").getTextContent()),
                new Filtered<>(n -> "lineItem".equals(n.getNodeName()), childNodes)
        );
    }

    @Override
    public LocalDate date() {
        return LocalDate.parse(textContent(date));
    }

    @Override
    public BigDecimal value() {
        return new BigDecimal(textContent(value));
    }

    private static String textContent(Scalar<Node> node) {
        return new Unchecked<>(node).value().getTextContent();
    }
}
