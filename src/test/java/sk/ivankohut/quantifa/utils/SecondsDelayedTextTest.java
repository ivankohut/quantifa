package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class SecondsDelayedTextTest {

    @Test
    void providesGivenTextAfterGivenNumberOfSeconds() throws Exception {
        var before = Instant.now();
        var text = "text";
        var delaySeconds = 1;
        var sut = new SecondsDelayedText(() -> text, delaySeconds);
        // exercise
        var result = sut.asString();
        // verify
        assertThat(result).isEqualTo(text);
        assertThat(Instant.now()).isAfterOrEqualTo(before.plusSeconds(delaySeconds));
    }
}
