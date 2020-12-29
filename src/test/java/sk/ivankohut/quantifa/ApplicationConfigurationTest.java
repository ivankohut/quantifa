package sk.ivankohut.quantifa;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApplicationConfigurationTest {

    @Test
    void twsCoordinatesValues() {
        // exercise
        var sut = create(Map.of(
                "TWS_HOSTNAME", "hostName",
                "TWS_PORT", "1234"
        ));
        // verify
        assertThat(sut.hostName()).isEqualTo("hostName");
        assertThat(sut.port()).isEqualTo(1234);
    }

    @Test
    void defaultHostName() {
        // exercise
        var sut = create(Map.of(
                "TWS_PORT", "1234"
        ));
        // verify
        assertThat(sut.hostName()).isEqualTo("localhost");
    }

    @Test
    void defaultPort() {
        // exercise
        var sut = create(Map.of(
                "TWS_HOSTNAME", "hostName"
        ));
        // verify
        assertThat(sut.port()).isEqualTo(7496);
    }

    @Test
    void stockContractValues() {
        // exercise
        var sut = create(Map.of(
                "EXCHANGE", "exchange",
                "SYMBOL", "symbol",
                "CURRENCY", "currency"
        ));
        // verify
        assertThat(sut.exchange()).isEqualTo("exchange");
        assertThat(sut.symbol()).isEqualTo("symbol");
        assertThat(sut.currency()).isEqualTo("currency");
    }

    private static ApplicationConfiguration create(Map<String, String> configuration) {
        return new ApplicationConfiguration("type", configuration);
    }

    @Test
    void failsIfStockContractValuesAreNotDefined() {
        // exercise
        var sut = new ApplicationConfiguration("environment variable", Map.of());
        // verify
        assertException(sut::exchange, "Configuration value EXCHANGE of type environment variable is not defined.");
        assertException(sut::symbol, "Configuration value SYMBOL of type environment variable is not defined.");
        assertException(sut::currency, "Configuration value CURRENCY of type environment variable is not defined.");
    }

    private static void assertException(ThrowableAssert.ThrowingCallable operation, String expectedMessage) {
        assertThatThrownBy(operation)
                .isInstanceOf(ApplicationException.class)
                .hasMessage(expectedMessage);
    }
}
