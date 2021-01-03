package sk.ivankohut.quantifa.decimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class RoundedTest {

    @Test
    void halfUpWithFourDecimalPlaces() throws Exception {
        var sut = new Rounded(() -> new BigDecimal("1.234567"));
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo("1.2346");
    }
}
