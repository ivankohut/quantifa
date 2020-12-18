package sk.ivankohut.quantifa;

import org.cactoos.Scalar;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.ScalarEnvelope;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.utils.StickyFirstOrFail;

import java.util.Comparator;

public class BookValueOfTheMostRecentBalanceSheet extends ScalarEnvelope<ReportedAmount> {

    public BookValueOfTheMostRecentBalanceSheet(Scalar<? extends Node> node, boolean annual) {
        super(
                new StickyFirstOrFail<>(
                        new Sorted<>(
                                Comparator.comparing(ReportedAmount::date).reversed(),
                                new Mapped<>(
                                        statementNode -> new XmlReportedAmount(statementNode, "STBP"),
                                        new StatementNodes(node, annual, true)
                                )
                        ),
                        "No financial statement available."
                )
        );
    }
}
