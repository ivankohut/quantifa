package sk.ivankohut.quantifa.utils;

import org.cactoos.iterable.IterableOf;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsNotEmptyTest {

    @Test
    void isNotEmptyIffContainsAtLeastOneElement() throws Exception {
        assertNotEmpty(false);
        assertNotEmpty(true, 1);
        assertNotEmpty(true, 1, 2);
    }

    private static void assertNotEmpty(boolean expected, Object... objects) throws Exception {
        var sut = new IsNotEmpty(new IterableOf<>(objects));
        // verify
        assertThat(sut.value()).isEqualTo(expected);
    }
}
