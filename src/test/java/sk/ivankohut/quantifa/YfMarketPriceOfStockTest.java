package sk.ivankohut.quantifa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class YfMarketPriceOfStockTest {

    @Test
    void currentPrice() {
        var sut = new YfMarketPriceOfStock(() -> """
                start root.App.main = {"context":{"dispatcher":{"stores":{"QuoteSummaryStore":{"financialData":{
                  "currentPrice": {"raw": 4.06, "fmt": "4.06" }
                }}}}}}; end
                """);
        // exercise
        var result = sut.price();
        // verify
        assertThat(result.get()).isEqualByComparingTo("4.06");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "start end",
            """
                    start root.App.main = {"context":{"dispatcher":{"stores":{"QuoteSummaryStore":{}}}}}; end
                    """,
            """
                    start root.App.main = {"context":{"dispatcher":{"stores":{"QuoteSummaryStore":{"financialDat; end
                    """,
            """
                    start root.App.main = {"context":{"dispatcher":{"stores":{"QuoteSummaryStore":{"financialDat; end
                    """,
    })
    void zeroIfPriceIsNotAvailable(String html) {
        var sut = new YfMarketPriceOfStock(() -> html);
        // exercise
        var result = sut.price();
        // verify
        assertThat(result).isEmpty();
    }
}
