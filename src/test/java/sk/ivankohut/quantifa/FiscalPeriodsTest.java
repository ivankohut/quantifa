package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.xmldom.XmlDocument;

import static org.assertj.core.api.Assertions.assertThat;

class FiscalPeriodsTest {

    @Test
    void periodsByGivenType() {
        var node = new XmlDocument(() -> """
                <root>
                    <AnnualPeriods>
                        <FiscalPeriod Type="Annual" EndDate="2019-12-31" FiscalYear="2019">
                            <Statement Type="INC" id="1">
                                <lineItem coaCode="I1">1.0</lineItem>
                            </Statement>
                            <Statement Type="BAL" id="2">
                                <lineItem coaCode="B1">2.0</lineItem>
                            </Statement>
                        </FiscalPeriod>
                        <FiscalPeriod Type="Annual" EndDate="2018-12-31" FiscalYear="2019">
                            <Statement Type="BAL" id="3">
                                <lineItem coaCode="B2">3.0</lineItem>
                            </Statement>
                            <Statement Type="INC" id="4">
                                <lineItem coaCode="I2">4.0</lineItem>
                            </Statement>
                        </FiscalPeriod>
                    </AnnualPeriods>
                    <InterimPeriods>
                        <FiscalPeriod Type="Annual" EndDate="2019-03-31" FiscalYear="2019">
                            <Statement Type="INC" id="1">
                                <lineItem coaCode="I3">5.0</lineItem>
                            </Statement>
                            <Statement Type="BAL" id="2">
                                <lineItem coaCode="B3">6.0</lineItem>
                            </Statement>
                        </FiscalPeriod>
                        <FiscalPeriod Type="Annual" EndDate="2018-09-30" FiscalYear="2019">
                            <Statement Type="BAL" id="3">
                                <lineItem coaCode="B4">7.0</lineItem>
                            </Statement>
                            <Statement Type="INC" id="4">
                                <lineItem coaCode="I4">8.0</lineItem>
                            </Statement>
                        </FiscalPeriod>
                    </InterimPeriods>
                </root>
                """).value().getFirstChild();
        // exercise
        // verify
        var annualIterator = new FiscalPeriods(() -> node, "Annual").iterator();
        var annualPeriod1 = annualIterator.next();
        assertThat(annualPeriod1.balanceSheet().date()).isEqualTo("2019-12-31");
        assertThat(annualPeriod1.balanceSheet().value("B1")).isEqualByComparingTo("2.0");
        assertThat(annualPeriod1.incomeStatement().date()).isEqualTo("2019-12-31");
        assertThat(annualPeriod1.incomeStatement().value("I1")).isEqualByComparingTo("1.0");
        var annualPeriod2 = annualIterator.next();
        assertThat(annualPeriod2.balanceSheet().date()).isEqualTo("2018-12-31");
        assertThat(annualPeriod2.balanceSheet().value("B2")).isEqualByComparingTo("3.0");
        assertThat(annualPeriod2.incomeStatement().date()).isEqualTo("2018-12-31");
        assertThat(annualPeriod2.incomeStatement().value("I2")).isEqualByComparingTo("4.0");
        assertThat(annualIterator).isExhausted();
        var interimIterator = new FiscalPeriods(() -> node, "Interim").iterator();
        var interimPeriod1 = interimIterator.next();
        assertThat(interimPeriod1.balanceSheet().date()).isEqualTo("2019-03-31");
        assertThat(interimPeriod1.balanceSheet().value("B3")).isEqualByComparingTo("6.0");
        assertThat(interimPeriod1.incomeStatement().date()).isEqualTo("2019-03-31");
        assertThat(interimPeriod1.incomeStatement().value("I3")).isEqualByComparingTo("5.0");
        var interimPeriod2 = interimIterator.next();
        assertThat(interimPeriod2.balanceSheet().date()).isEqualTo("2018-09-30");
        assertThat(interimPeriod2.balanceSheet().value("B4")).isEqualByComparingTo("7.0");
        assertThat(interimPeriod2.incomeStatement().date()).isEqualTo("2018-09-30");
        assertThat(interimPeriod2.incomeStatement().value("I4")).isEqualByComparingTo("8.0");
        assertThat(interimIterator).isExhausted();
    }
}
