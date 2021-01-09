package sk.ivankohut.quantifa.decimal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class NonNegativeTest {
    @ParameterizedTest
    @CsvSource({
            "-1.5, 0",
            "0, 0",
            "4.2, 4.2"
    })
    void nonNegative(BigDecimal decimal, BigDecimal expectedNonNegative) throws Exception {
        var sut = new NonNegative(() -> decimal);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo(expectedNonNegative);
    }
}
