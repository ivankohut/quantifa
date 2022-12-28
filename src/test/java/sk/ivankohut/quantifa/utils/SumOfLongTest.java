package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SumOfLongTest {

    @Test
    void sumOfGivenLongScalars() throws Exception {
        var sut = new SumOfLong(() -> 1L, () -> 2L);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo(3);
    }
}
