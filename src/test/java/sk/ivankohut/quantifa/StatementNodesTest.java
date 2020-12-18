package sk.ivankohut.quantifa;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.xmldom.XmlDocument;

import static org.assertj.core.api.Assertions.assertThat;

class StatementNodesTest {

    @ParameterizedTest
    @CsvSource({
            "true,  true,  ab",
            "true,  false, ai",
            "false, true,  ib",
            "false, false, ii"
    })
    void statementNodesOfGivenPeriodAndType(boolean annual, boolean balanceSheet, String expectedTextContentPrefix) {
        // exercise
        var sut = new StatementNodes(() -> new XmlDocument(() -> """
                <root>
                    <AnnualPeriods>
                        <FiscalPeriod>
                            <Statement Type="INC">ai1</Statement>
                            <Statement Type="BAL">ab1</Statement>
                        </FiscalPeriod>
                        <FiscalPeriod>
                            <Statement Type="INC">ai2</Statement>
                            <Statement Type="BAL">ab2</Statement>
                        </FiscalPeriod>
                    </AnnualPeriods>
                    <InterimPeriods>
                        <FiscalPeriod>
                            <Statement Type="INC">ii1</Statement>
                            <Statement Type="BAL">ib1</Statement>
                        </FiscalPeriod>
                        <FiscalPeriod>
                            <Statement Type="INC">ii2</Statement>
                            <Statement Type="BAL">ib2</Statement>
                        </FiscalPeriod>
                    </InterimPeriods>
                </root>
                """).value().getDocumentElement(), annual, balanceSheet);
        // verify
        assertThat(sut).extracting(Node::getTextContent).containsExactly(expectedTextContentPrefix + "1", expectedTextContentPrefix + "2");
    }
}
