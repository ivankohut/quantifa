package sk.ivankohut.quantifa;

import org.cactoos.scalar.ScalarEnvelope;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.xmldom.ChildNodes;
import sk.ivankohut.quantifa.xmldom.FirstFilteredNode;

import java.time.LocalDate;

public class XmlStatementDate extends ScalarEnvelope<LocalDate> {

    public XmlStatementDate(Node statementNode) {
        super(
                new org.cactoos.scalar.Mapped<>(
                        dateNode -> LocalDate.parse(dateNode.getTextContent()),
                        new FirstFilteredNode(
                                "StatementDate",
                                new ChildNodes(new FirstFilteredNode("FPHeader", new ChildNodes(statementNode)))
                        )
                )
        );
    }
}
