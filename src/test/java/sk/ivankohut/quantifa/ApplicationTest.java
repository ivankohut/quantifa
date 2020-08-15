package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

    @Test
    void appHasAGreeting() {
        var sut = new Application("quantifa");
        // exercise
        var result = sut.greeting();
        // verify
        assertThat(result).isEqualTo("Hello from quantifa");
    }
}
