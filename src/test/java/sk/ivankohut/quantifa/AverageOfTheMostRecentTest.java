package sk.ivankohut.quantifa;

import org.cactoos.list.ListOf;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AverageOfTheMostRecentTest {

    @Test
    void averageOfTheGivenNumberOfTheMostRecentAmounts() throws Exception {
        // exercise
        var sut = new AverageOfTheMostRecent(new ListOf<>(
                new SimpleReportedAmount(LocalDate.of(2000, 1, 2), BigDecimal.ONE),
                new SimpleReportedAmount(LocalDate.of(2000, 1, 4), BigDecimal.ZERO),
                new SimpleReportedAmount(LocalDate.of(2000, 1, 1), BigDecimal.valueOf(2)),
                new SimpleReportedAmount(LocalDate.of(2000, 1, 3), BigDecimal.TEN)
        ), 2);
        // verify
        assertThat(sut.value()).isEqualByComparingTo("5");
    }

    @Test
    void averageOfAllTheGivenAmountsIfTheirNumberIsLessThenRequested() throws Exception {
        // exercise
        var sut = new AverageOfTheMostRecent(new ListOf<>(
                new SimpleReportedAmount(LocalDate.of(2000, 1, 1), BigDecimal.ONE)
        ), 2);
        // verify
        assertThat(sut.value()).isEqualByComparingTo("1");
    }
}
