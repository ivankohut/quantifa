package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleStockContractTest {

    @Test
    void providesGivenValues() {
        var exchange = "exchange";
        var symbol = "symbol";
        var currency = "currency";
        var sut = new SimpleStockContract(exchange, symbol, currency);
        // exercise
        // verify
        assertThat(sut.exchange()).isEqualTo(exchange);
        assertThat(sut.symbol()).isEqualTo(symbol);
        assertThat(sut.currency()).isEqualTo(currency);
    }
}
