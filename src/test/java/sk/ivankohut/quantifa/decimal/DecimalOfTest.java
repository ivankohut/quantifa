package sk.ivankohut.quantifa.decimal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DecimalOfTest {

    @Test
    void fromNumber() throws Exception {
        var sut = new DecimalOf(123.45);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualByComparingTo("123.45");
    }
}
