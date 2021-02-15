package sk.ivankohut.quantifa.decimal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class NegatedTest {

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "-1,1",
            "1,-1"
    })
    void negated(BigDecimal value, BigDecimal expected) throws Exception {
        var sut = new Negated(() -> value);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo(expected);
    }
}
