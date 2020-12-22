package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeParseExceptionToIterableTest {

    @Test
    void providedGivenLocalDate() {
        var localDate = LocalDate.now();
        // exercise
        var sut = new DateTimeParseExceptionToIterable<>(() -> localDate);
        // verify
        assertThat(sut).containsExactly(localDate);
    }

    @Test
    void emptyIteratorIfDateTimeParseExceptionIsThrown() {
        // exercise
        var sut = new DateTimeParseExceptionToIterable<>(() -> LocalDate.parse("abc"));
        // verify
        assertThat(sut).isEmpty();
    }
}
