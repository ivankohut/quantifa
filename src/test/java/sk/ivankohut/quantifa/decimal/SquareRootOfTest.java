package sk.ivankohut.quantifa.decimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SquareRootOfTest {

    @Test
    void squareRootOfGivenNumberUsingDECIMAL128PrecisionSetting() throws Exception {
        var sut = new SquareRootOf(BigDecimal.TEN);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("3.162277660168379331998893544432719");
    }
}
