package sk.ivankohut.quantifa;

import org.cactoos.list.ListOf;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MostRecentFinancialStatementTest {

    @Test
    void mostRecentFinancialStatement() throws Exception {
        var date = LocalDate.now();
        var value = BigDecimal.TEN;
        var sut = new MostRecentFinancialStatement(new ListOf<>(
                createFinancialStatement(date.minusDays(1), value.add(BigDecimal.ONE)),
                createFinancialStatement(date, value),
                createFinancialStatement(date.minusDays(2), value.add(BigDecimal.TEN))
        ));
        // exercise
        // verify
        assertThat(sut.date()).isEqualTo(date);
        assertThat(sut.value("name").value()).isEqualByComparingTo(value);
    }

    @Test
    void failsIfNoAnyFinancialStatementExist() {
        var sut = new MostRecentFinancialStatement(new ListOf<>());
        // exercise
        // verify
        assertThatThrownBy(sut::date)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No any financial statement available - not possible to choose the most recent one.");
    }

    private static FinancialStatement createFinancialStatement(LocalDate date, BigDecimal value) {
        var result = mock(FinancialStatement.class);
        when(result.date()).thenReturn(date);
        when(result.value(anyString())).thenReturn(() -> value);
        return result;
    }
}
