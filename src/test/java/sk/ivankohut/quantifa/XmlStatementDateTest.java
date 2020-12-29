package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.xmldom.XmlDocument;

import static org.assertj.core.api.Assertions.assertThat;

class XmlStatementDateTest {

    @Test
    void dateFromHeader() throws Exception {
        var sut = new XmlStatementDate(new XmlDocument(() -> """
                        <Statement Type="INC">
                            <FPHeader>
                                <StatementDate>2019-12-31</StatementDate>
                            </FPHeader>
                            <lineItem coaCode="CMIN">-1.000000</lineItem>
                        </Statement>
                """).value().getFirstChild());
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo("2019-12-31");
    }
}
