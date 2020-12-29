package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FinancialStatementAmountTest {

    @Test
    void providesDateAndValueOfGivenLineItem() {
        var amount = BigDecimal.TEN;
        var date = LocalDate.parse("2020-01-05");
        var lineItem = "lineItem";
        var statement = mock(FinancialStatement.class);
        when(statement.value(lineItem)).thenReturn(amount);
        when(statement.date()).thenReturn(date);
        // exercise
        var sut = new FinancialStatementAmount(statement, lineItem);
        // verify
        assertThat(sut.date()).isEqualTo(date);
        assertThat(sut.value()).isEqualTo(amount);
    }
}
