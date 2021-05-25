package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.xmldom.XmlDocument;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class XmlFinancialStatementTest {

    @Test
    void extractLineItemFromGivenXml() throws Exception {
        // exercise
        var sut = new XmlFinancialStatement(
                () -> LocalDate.parse("2019-12-31"),
                () -> new XmlDocument(() -> """
                                <Statement Type="INC">
                                    <FPHeader>
                                        <StatementDate>2019-12-31</StatementDate>
                                    </FPHeader>
                                    <lineItem coaCode="ITEM1">1.1</lineItem>
                                    <lineItem coaCode="ITEM2">2.2</lineItem>
                                    <lineItem coaCode="ITEM3">3.3</lineItem>
                                </Statement>
                        """).value().getFirstChild()
        );
        // verify
        assertThat(sut.date()).isEqualTo("2019-12-31");
        assertThat(sut.value("ITEM2").value()).isEqualByComparingTo(BigDecimal.valueOf(2.2));
        assertThat(sut.value("ITEM0").value()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
