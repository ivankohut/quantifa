package sk.ivankohut.quantifa;

import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.ScalarEnvelope;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.utils.StickyFirstOrFail;

import java.time.LocalDate;
import java.util.Map;

public class MostRecentFinancialStatementNode extends ScalarEnvelope<Map.Entry<LocalDate, Node>> {

    public MostRecentFinancialStatementNode(Iterable<Node> nodes) {
        super(new StickyFirstOrFail<>(
                new Sorted<>(
                        (e1, e2) -> e2.getKey().compareTo(e1.getKey()),
                        new Mapped<>(
                                statementNode -> Map.entry(new XmlStatementDate(statementNode).value(), statementNode),
                                nodes
                        )
                ),
                "No financial statement available."
        ));
    }
}
