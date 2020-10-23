package sk.ivankohut.quantifa;

import com.ib.client.TickType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MarketDataHandlerTest {

    @ParameterizedTest
    @CsvSource({
            "true, DELAYED_BID",
            "false, DELAYED_ASK"
    })
    void nonRequestedPricesIgnored(boolean isBid, TickType nonIgnoredTickType) {
        var sut = new MarketDataHandler(isBid);
        var price = 10.0;
        // exercise
        sut.tickPrice(TickType.DELAYED_LOW, price - 1, null);
        sut.tickPrice(nonIgnoredTickType, price, null);
        sut.tickPrice(TickType.DELAYED_HIGH, price + 1, null);
        var result = sut.price();
        // verify
        assertThat(result.get()).isEqualByComparingTo(BigDecimal.valueOf(price));
    }

    @Test
    void noPriceIfThereIsNoPriceInTws() {
        var sut = new MarketDataHandler(true);
        // exercise
        sut.tickPrice(TickType.DELAYED_BID, -1.0, null);
        var result = sut.price();
        // verify
        assertThat(result).isEmpty();
    }
}
