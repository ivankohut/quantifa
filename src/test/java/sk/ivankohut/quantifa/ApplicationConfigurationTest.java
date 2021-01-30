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
    void fundamentalsRequestValues() {
        var sut = create(Map.of(
                "FUNDAMENTALS_EXCHANGE", "exchange",
                "FUNDAMENTALS_SYMBOL", "symbol",
                "FUNDAMENTALS_CURRENCY", "currency"
        ));
        // exercise
        var result = sut.fundamentalsRequest();
        // verify
        assertThat(result.exchange()).isEqualTo("exchange");
        assertThat(result.symbol()).isEqualTo("symbol");
        assertThat(result.currency()).isEqualTo("currency");
    }

    @Test
    void failsIfFundamentalsValuesAreNotDefined() {
        var sut = new ApplicationConfiguration("environment variable", Map.of());
        // exercise
        var result = sut.fundamentalsRequest();
        // verify
        assertException(result::exchange, "Configuration value FUNDAMENTALS_EXCHANGE of type environment variable is not defined.");
        assertException(result::symbol, "Configuration value FUNDAMENTALS_SYMBOL of type environment variable is not defined.");
        assertException(result::currency, "Configuration value FUNDAMENTALS_CURRENCY of type environment variable is not defined.");
    }

    @Test
    void priceRequestValues() {
        var sut = create(Map.of(
                "PRICE_SOURCE", "source",
                "PRICE_APIKEY", "apiKey",
                "PRICE_SYMBOL", "symbol",
                "PRICE_CURRENCY", "currency",
                "PRICE_DIVISOR", "100"
        ));
        // exercise
        var result = sut.priceRequest();
        // verify
        assertThat(result.source()).isEqualTo("source");
        assertThat(result.apiKey()).isEqualTo("apiKey");
        assertThat(result.symbol()).isEqualTo("symbol");
        assertThat(result.currency()).isEqualTo("currency");
        assertThat(result.divisor()).isEqualTo(100);
    }

    @Test
    void failsIfPriceRequestMandatoryValueAreNotDefined() {
        var sut = new ApplicationConfiguration("environment variable", Map.of());
        // exercise
        var result = sut.priceRequest();
        // verify
        assertException(result::source, "Configuration value PRICE_SOURCE of type environment variable is not defined.");
        assertException(result::symbol, "Configuration value PRICE_SYMBOL of type environment variable is not defined.");
        assertException(result::currency, "Configuration value PRICE_CURRENCY of type environment variable is not defined.");
    }

    @Test
    void defaultPriceRequestValues() {
        var sut = create(Map.of());
        // exercise
        var result = sut.priceRequest();
        // verify
        assertThat(result.apiKey()).isEmpty();
        assertThat(result.divisor()).isEqualTo(1);
    }

    private static ApplicationConfiguration create(Map<String, String> configuration) {
        return new ApplicationConfiguration("type", configuration);
    }

    private static void assertException(ThrowableAssert.ThrowingCallable operation, String expectedMessage) {
        assertThatThrownBy(operation)
                .isInstanceOf(ApplicationException.class)
                .hasMessage(expectedMessage);
    }
}
