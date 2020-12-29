package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TrailingTwelveMonthsTest {

    @Test
    void sumsValuesReportedInLastYear() throws Exception {
        var sut = new TrailingTwelveMonths(List.of(
                createReportedAmount("2020-06-30", 1.0),
                createReportedAmount("2019-11-30", 2.0),
                createReportedAmount("2019-06-30", 10.0)
        ));
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("3");
    }

    private static ReportedAmount createReportedAmount(String date, double value) {
        return new SimpleReportedAmount(LocalDate.parse(date), BigDecimal.valueOf(value));
    }
}
