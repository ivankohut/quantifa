package sk.ivankohut.quantifa;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sk.ivankohut.quantifa.xmldom.XmlDocument;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MostRecentFinancialStatementNodeTest {

    @ParameterizedTest
    @CsvSource({
            "AnnualPeriods,  true,  true,  2, 2019-12-31",
            "InterimPeriods, false, true,  2, 2019-12-31",
            "AnnualPeriods,  true,  false, 4, 2020-12-31",
            "InterimPeriods, false, false, 4, 2020-12-31"
    })
    void annualBalanceSheets(String nodeName, boolean annual, boolean balanceSheet, String expectedId, LocalDate expectedDate) throws Exception {
        var sut = new MostRecentFinancialStatementNode(new StatementNodes(new XmlDocument(() -> """
                <%s>
                    <FiscalPeriod Type="Annual" EndDate="2019-12-31" FiscalYear="2019">
                        <Statement Type="INC" id="1">
                            <FPHeader>
                                <StatementDate>2019-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="CMIN">-1.000000</lineItem>
                        </Statement>
                        <Statement Type="BAL" id="2">
                            <FPHeader>
                                <StatementDate>2019-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="QTCO">550.082610</lineItem>
                            <lineItem coaCode="STBP">12.3</lineItem>
                        </Statement>
                        <Statement Type="BAL" id="3">
                            <FPHeader>
                                <StatementDate>2018-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="QTCO">550.082610</lineItem>
                            <lineItem coaCode="STBP">15.4</lineItem>
                        </Statement>
                        <Statement Type="INC" id="4">
                            <FPHeader>
                                <StatementDate>2020-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="CMIN">-1.000000</lineItem>
                        </Statement>
                    </FiscalPeriod>
                </%s>
                """.formatted(nodeName, nodeName)), annual, balanceSheet));

        // exercise
        var result = sut.value();
        // verify
        assertThat(result.getValue().getAttributes().getNamedItem("id").getTextContent()).isEqualTo(expectedId);
        assertThat(result.getKey()).isEqualTo(expectedDate);
    }
}
