package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KeyOnlyEntryTest {

    @Test
    void providesKeyOnly() {
        var key = "key";
        var sut = new KeyOnlyEntry<>(key);
        // exercise
        // verify
        assertThat(sut.getKey()).isEqualTo(key);
        assertThatThrownBy(sut::getValue).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> sut.setValue("")).isInstanceOf(UnsupportedOperationException.class);
    }
}
