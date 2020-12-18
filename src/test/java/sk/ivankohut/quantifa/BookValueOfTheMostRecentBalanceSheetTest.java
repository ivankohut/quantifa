package sk.ivankohut.quantifa;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sk.ivankohut.quantifa.xmldom.XmlDocument;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BookValueOfTheMostRecentBalanceSheetTest {

    @ParameterizedTest
    @CsvSource({
            "AnnualPeriods,  true",
            "InterimPeriods, false"
    })
    void annualBalanceSheets(String nodeName, boolean annual) throws Exception {
        var sut = new BookValueOfTheMostRecentBalanceSheet(new XmlDocument(() -> """
                <%s>
                    <FiscalPeriod Type="Annual" EndDate="2019-12-31" FiscalYear="2019">
                        <Statement Type="INC">
                            <FPHeader>
                                <StatementDate>2019-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="CMIN">-1.000000</lineItem>
                        </Statement>
                        <Statement Type="BAL">
                            <FPHeader>
                                <StatementDate>2019-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="QTCO">550.082610</lineItem>
                            <lineItem coaCode="STBP">12.3</lineItem>
                        </Statement>
                        <Statement Type="BAL">
                            <FPHeader>
                                <StatementDate>2018-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="QTCO">550.082610</lineItem>
                            <lineItem coaCode="STBP">15.4</lineItem>
                        </Statement>
                    </FiscalPeriod>
                </%s>
                """.formatted(nodeName, nodeName)), annual);

        // exercise
        var result = sut.value();
        // verify
        assertThat(result.value()).isEqualByComparingTo(new BigDecimal("12.3"));
        assertThat(result.date()).isEqualTo("2019-12-31");
    }
}
