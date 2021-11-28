package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class DelayedScalarTest {

    @Test
    void providesGivenValueAfterGivenNumberOfMilliSeconds() throws Exception {
        var before = Instant.now();
        var value = "value";
        var delay = 500;
        var sut = new DelayedScalar<>(() -> value, delay);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo(value);
        assertThat(Instant.now()).isAfterOrEqualTo(before.plusMillis(delay));
    }
}
