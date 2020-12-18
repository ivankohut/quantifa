package sk.ivankohut.quantifa;

import org.cactoos.Scalar;
import org.cactoos.iterable.IterableEnvelope;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.xmldom.XPathNodes;

public class StatementNodes extends IterableEnvelope<Node> {

    public StatementNodes(Scalar<? extends Node> node, boolean annual, boolean balanceSheet) {
        super(new XPathNodes(
                node,
                (annual ? "Annual" : "Interim") + "Periods/FiscalPeriod/Statement[@Type='" + (balanceSheet ? "BAL" : "INC") + "']"
        ));
    }
}
