package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationExceptionTest {

    @Test
    void causeIsGivenCause() {
        var cause = new RuntimeException();
        // exercise
        var sut = new ApplicationException(cause);
        // verify
        assertThat(sut.getCause()).isEqualTo(cause);
    }
}
