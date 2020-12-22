package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateOfTest {

    @Test
    void parsedLocalDateFromGivenText() throws Exception {
        var dateString = "2020-01-24";
        var sut = new LocalDateOf(() -> dateString);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo(dateString);
    }
}
