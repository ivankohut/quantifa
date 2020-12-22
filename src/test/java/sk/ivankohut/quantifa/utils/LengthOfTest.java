package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LengthOfTest {

    @Test
    void lengthOfGivenText() throws Exception {
        var sut = new LengthOf(() -> "text");
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo(4);
    }
}
