package sk.ivankohut.quantifa.decimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SumOfTest {

    @Test
    void sumOfGivenAddends() throws Exception {
        var sut = new SumOf(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.valueOf(5.4));
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("16.4");
    }

    @Test
    void zeroIfNoAddendsGiven() throws Exception {
        var sut = new SumOf();
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("0");
    }
}
