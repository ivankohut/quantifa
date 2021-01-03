package sk.ivankohut.quantifa.decimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DivisionOfTest {

    @Test
    void dividesGivenDividendByGivenDivisorUsingDECIMAL128PrecisionSetting() throws Exception {
        var sut = new DivisionOf(BigDecimal.valueOf(10), BigDecimal.valueOf(3));
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("3.333333333333333333333333333333333");
    }
}
