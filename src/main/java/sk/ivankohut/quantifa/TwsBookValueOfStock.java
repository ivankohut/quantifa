package sk.ivankohut.quantifa;

import com.ib.client.Types;
import org.cactoos.Scalar;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.ScalarEnvelope;
import org.cactoos.scalar.ScalarOfSupplier;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.utils.StickyFirstOrFail;
import sk.ivankohut.quantifa.xmldom.ChildNodes;
import sk.ivankohut.quantifa.xmldom.FirstFilteredNode;
import sk.ivankohut.quantifa.xmldom.XPathNodes;

import java.util.Comparator;

public class TwsBookValueOfStock extends ScalarEnvelope<ReportedAmount> {

    public TwsBookValueOfStock(TwsApi twsApi, StockContract stockContract) {
        super(create(twsApi, stockContract));
    }

    private static Scalar<ReportedAmount> create(TwsApi twsApi, StockContract stockContract) {
        var fundamental = new TwsFundamental(twsApi, stockContract, Types.FundamentalType.ReportsFinStatements);
        var financialStatementsNode = new StickyFirstOrFail<>(
                new XPathNodes(fundamental, "/ReportFinancialStatements/FinancialStatements"),
                "No financial statements available."
        );
        var annual = new Unchecked<>(new MostRecentBalanceSheet(financialStatementsNode, true));
        var interim = new Unchecked<>(new MostRecentBalanceSheet(financialStatementsNode, false));
        return new Ternary<>(new ScalarOfSupplier<>(() -> interim.value().date().isAfter(annual.value().date())), interim, annual);
    }
}

class MostRecentBalanceSheet extends ScalarEnvelope<ReportedAmount> {

    MostRecentBalanceSheet(Scalar<? extends Node> node, boolean annual) {
        super(
                new StickyFirstOrFail<>(
                        new Sorted<>(
                                Comparator.comparing(ReportedAmount::date).reversed(),
                                new Mapped<>(
                                        statementNode -> {
                                            var childNodes = new ChildNodes(statementNode);
                                            return new XmlReportedAmount(
                                                    new FirstFilteredNode(
                                                            "StatementDate",
                                                            new ChildNodes(new FirstFilteredNode("FPHeader", childNodes))
                                                    ),
                                                    new FirstFilteredNode(
                                                            n -> "STBP".equals(n.getAttributes().getNamedItem("coaCode").getTextContent()),
                                                            new Filtered<>(n -> "lineItem".equals(n.getNodeName()), childNodes)
                                                    )
                                            );
                                        },
                                        new XPathNodes(
                                                node,
                                                (annual ? "Annual" : "Interim") + "Periods/FiscalPeriod/Statement[@Type='BAL']"
                                        )
                                )
                        ),
                        "No financial statement available."
                )
        );
    }
}
