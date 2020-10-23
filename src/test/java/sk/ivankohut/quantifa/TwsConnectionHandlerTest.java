package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TwsConnectionHandlerTest {

    @Test
    void doesNothingAtAll() {
        var sut = new TwsConnectionHandler();
        // exercise
        sut.accountList(List.of());
        sut.connected();
        sut.disconnected();
        sut.error(new Exception());
        sut.show("string");
        sut.message(0, 0, "errorMsg");
        // verify
        assertThat(sut).isNotNull();
    }
}
