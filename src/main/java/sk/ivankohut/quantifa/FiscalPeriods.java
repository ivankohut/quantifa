package sk.ivankohut.quantifa;

import org.cactoos.Scalar;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.And;
import org.cactoos.scalar.Equals;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.utils.IsNotEmpty;
import sk.ivankohut.quantifa.xmldom.FirstFilteredNode;
import sk.ivankohut.quantifa.xmldom.ListOfNodes;
import sk.ivankohut.quantifa.xmldom.XPathNodes;

import java.time.LocalDate;
import java.util.Map;

public class FiscalPeriods extends IterableEnvelope<FiscalPeriod> {

    public FiscalPeriods(Scalar<? extends Node> node, String type) {
        super(
                new Mapped<>(
                        fiscalPeriod -> new FiscalPeriod() {

                            private final FinancialStatement balanceSheet = createFinancialStatement("BAL");
                            private final FinancialStatement incomeStatement = createFinancialStatement("INC");

                            private XmlFinancialStatement createFinancialStatement(String statementType) {
                                return new XmlFinancialStatement(
                                        fiscalPeriod::getKey,
                                        new FirstFilteredNode(
                                                n -> true,
                                                new StatementsOfType(fiscalPeriod.getValue(), statementType)
                                        )
                                );
                            }

                            @Override
                            public FinancialStatement balanceSheet() {
                                return balanceSheet;
                            }

                            @Override
                            public FinancialStatement incomeStatement() {
                                return incomeStatement;
                            }
                        },
                        new Filtered<Map.Entry<LocalDate, Iterable<Node>>>(
                                fiscalPeriod -> new And(
                                        new IsNotEmpty(new StatementsOfType(fiscalPeriod.getValue(), "BAL")),
                                        new IsNotEmpty(new StatementsOfType(fiscalPeriod.getValue(), "INC"))
                                ).value(),
                                new Mapped<>(
                                        fiscalPeriodNode -> Map.entry(
                                                LocalDate.parse(fiscalPeriodNode.getAttributes().getNamedItem("EndDate").getTextContent()),
                                                new ListOfNodes(fiscalPeriodNode.getChildNodes())
                                        ),
                                        new XPathNodes(node, type + "Periods/FiscalPeriod")
                                )
                        )
                )
        );
    }

    private static class StatementsOfType extends IterableEnvelope<Node> {

        public StatementsOfType(Iterable<Node> nodes, String type) {
            super(new Filtered<>(
                    nodes,
                    statementNode -> new And(
                            new Equals<String, String>(statementNode::getNodeName, () -> "Statement"),
                            new Equals<String, String>(() -> statementNode.getAttributes().getNamedItem("Type").getNodeValue(), () -> type)
                    )
            ));
        }
    }
}
