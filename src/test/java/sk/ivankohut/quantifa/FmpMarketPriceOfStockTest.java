package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FmpMarketPriceOfStockTest {

    @Test
    void priceOfGivenSymbol() {
        var sut = new FmpMarketPriceOfStock(
                () -> """
                        [ {
                          "symbol" : "SPY",
                          "name" : "SPDR S&P 500",
                          "price" : 370.07,
                          "exchange" : "NYSE Arca"
                        }, {
                          "symbol" : "CMCSA",
                          "name" : "Comcast Corp",
                          "price" : 49.57,
                          "exchange" : "Nasdaq Global Select"
                        }, {
                          "symbol" : "KMI",
                          "name" : "Kinder Morgan Inc",
                          "price" : 14.08,
                          "exchange" : "New York Stock Exchange"
                        } ]""",
                "CMCSA"
        );
        // exercise
        var result = sut.price();
        // verify
        assertThat(result.get()).isEqualByComparingTo("49.57");
    }

    @Test
    void noneIfNoPriceForGivenSymbolExists() {
        var sut = new FmpMarketPriceOfStock(
                () -> """
                        [ {
                          "symbol" : "KMI",
                          "name" : "Kinder Morgan Inc",
                          "price" : 14.08,
                          "exchange" : "New York Stock Exchange"
                        } ]""",
                "CMCSA"
        );
        // exercise
        var result = sut.price();
        // verify
        assertThat(result).isEmpty();
    }
}
