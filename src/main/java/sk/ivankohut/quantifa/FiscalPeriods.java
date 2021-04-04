package sk.ivankohut.quantifa;

import org.cactoos.Scalar;
import org.cactoos.iterable.IterableEnvelope;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.xmldom.FirstFilteredNode;
import sk.ivankohut.quantifa.xmldom.XPathNodes;

import java.time.LocalDate;

public class FiscalPeriods extends IterableEnvelope<FiscalPeriod> {

    public FiscalPeriods(Scalar<? extends Node> node, String type) {
        super(
                new org.cactoos.iterable.Mapped<>(
                        fiscalPeriodNode -> new FiscalPeriod() {

                            private final Scalar<LocalDate> date = () -> LocalDate.parse(
                                    fiscalPeriodNode.getAttributes().getNamedItem("EndDate").getTextContent()
                            );
                            private final FinancialStatement balanceSheet = createFinancialStatement("BAL");
                            private final FinancialStatement incomeStatement = createFinancialStatement("INC");

                            private XmlFinancialStatement createFinancialStatement(String statementType) {
                                return new XmlFinancialStatement(
                                        date,
                                        new FirstFilteredNode(
                                                n -> true,
                                                new XPathNodes(() -> fiscalPeriodNode, "Statement[@Type='" + statementType + "']")
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
                        new XPathNodes(node, type + "Periods/FiscalPeriod")
                )
        );
    }
}
