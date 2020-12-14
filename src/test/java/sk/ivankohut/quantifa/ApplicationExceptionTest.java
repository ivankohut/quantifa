package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationExceptionTest {

    @Test
    void hasGivenExceptionAsCause() {
        var cause = new RuntimeException();
        // exercise
        var sut = new ApplicationException(cause);
        // verify
        assertThat(sut.getCause()).isEqualTo(cause);
    }

    @Test
    void hasGivenStringAsMessage() {
        var message = "message";
        // exercise
        var sut = new ApplicationException(message);
        // verify
        assertThat(sut.getMessage()).isEqualTo(message);
    }
}
