package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.ApplicationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StickyFirstOrFailTest {

    @Test
    void firstOfGivenItems() throws Exception {
        var sut = new StickyFirstOrFail<>(List.of("a", "b"), "");
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo("a");
    }

    @Test
    void exceptionWithGivenMessageIfThereAreNoAnyItems() {
        var missingMessage = "missingMessage";
        var sut = new StickyFirstOrFail<>(List.of(), missingMessage);
        // exercise
        // verify
        assertThatThrownBy(sut::value)
                .isInstanceOf(ApplicationException.class)
                .hasMessage(missingMessage);
    }
}
