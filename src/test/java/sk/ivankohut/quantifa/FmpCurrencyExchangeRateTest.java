package sk.ivankohut.quantifa;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("PMD.ShortVariable")
class FmpCurrencyExchangeRateTest {

    @ParameterizedTest
    @CsvSource({
            "EUR,EUR,1.0",
            "EUR,USD,1.18382",
            "JPY,USD,0.009409",
            "GBP,USD,1.32538"
    })
    void bidForGivenCurrencyPair(String from, String to, BigDecimal expectedRate) {
        var sut = new FmpCurrencyExchangeRate(() -> """
                [ {
                    "ticker" : "EUR/USD",
                    "bid" : "1.18382",
                    "ask" : "1.18386",
                    "open" : "1.18458",
                    "low" : "1.18193",
                    "high" : "1.18837",
                    "changes" : -0.062469398436573544,
                    "date" : "2020-09-06 20:41:57"
                   }, {
                      "ticker" : "USD/JPY",
                      "bid" : "106.283",
                      "ask" : "106.288",
                      "open" : "106.262",
                      "low" : "106.105",
                      "high" : "106.403",
                      "changes" : 0.02211514934783697,
                      "date" : "2020-09-06 20:41:57"
                    }, {
                      "ticker" : "USD/GPB",
                      "bid" : "7.123",
                      "ask" : "1.32547",
                      "open" : "1.32659",
                      "low" : "1.32258",
                      "high" : "1.32916",
                      "changes" : -0.0878191453274833,
                      "date" : "2020-09-06 20:41:57"
                    }, {
                      "ticker" : "GBP/USD",
                      "bid" : "1.32538",
                      "ask" : "1.32547",
                      "open" : "1.32659",
                      "low" : "1.32258",
                      "high" : "1.32916",
                      "changes" : -0.0878191453274833,
                      "date" : "2020-09-06 20:41:57"
                    }
                  ]""",
                from, to
        );
        // exercise
        var result = sut.price().get();
        // verify
        assertThat(result).isEqualByComparingTo(expectedRate);
    }
}
