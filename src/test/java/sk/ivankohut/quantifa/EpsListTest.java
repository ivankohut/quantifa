package sk.ivankohut.quantifa;

import org.cactoos.list.ListOf;
import org.cactoos.scalar.Unchecked;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EpsListTest {

    @Test
    void valuesFromIncomeStatements() {
        var date1 = LocalDate.now();
        var date2 = date1.plusDays(1);
        var lineItem = "VDES";
        var value1 = BigDecimal.ONE;
        var value2 = BigDecimal.TEN;
        // exercise
        var sut = new EpsList(new ListOf<>(
                createFiscalPeriod(date1, lineItem, value1),
                createFiscalPeriod(date2, lineItem, value2)
        ));
        // verify
        assertThat(sut).extracting(FinancialStatementAmount::date).containsExactly(date1, date2);
        assertThat(sut).extracting(amount -> new Unchecked<>(amount.value()).value()).containsExactly(value1, value2);
    }

    private static FiscalPeriod createFiscalPeriod(LocalDate date, String lineItem, BigDecimal value) {
        var incomeStatement = mock(FinancialStatement.class);
        when(incomeStatement.date()).thenReturn(date);
        when(incomeStatement.value(lineItem)).thenReturn(() -> value);
        return when(mock(FiscalPeriod.class).incomeStatement()).thenReturn(incomeStatement).getMock();
    }
}
