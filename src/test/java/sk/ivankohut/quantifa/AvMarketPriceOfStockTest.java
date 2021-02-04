package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AvMarketPriceOfStockTest {

    @Test
    void priceOfGivenSymbol() {
        var sut = new AvMarketPriceOfStock(() -> """
                {
                  "Global Quote": {
                    "01. symbol": "AEU.F",
                    "02. open": "26.9900",
                    "03. high": "27.1900",
                    "04. low": "26.7700",
                    "05. price": "26.7700",
                    "06. volume": "29",
                    "07. latest trading day": "2021-01-29",
                    "08. previous close": "27.2200",
                    "09. change": "-0.4500",
                    "10. change percent": "-1.6532%"
                  }
                }""");
        // exercise
        var result = sut.price();
        // verify
        assertThat(result.get()).isEqualByComparingTo("26.77");
    }

    @Test
    void noneIfNoPriceForGivenSymbolExists() {
        var sut = new AvMarketPriceOfStock(() -> """
                {
                  "Global Quote": {}
                }""");
        // exercise
        var result = sut.price();
        // verify
        assertThat(result).isEmpty();
    }
}
