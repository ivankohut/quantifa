package sk.ivankohut.quantifa;

import com.ib.client.MarketDataType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

public class TwsMarketPriceOfStock implements MarketPrice {

    private final TwsApi twsApi;
    private final StockContract stockContract;
    private final boolean bid;
    private final int divisor;

    public TwsMarketPriceOfStock(TwsApi twsApi, StockContract stockContract, boolean bid, int divisor) {
        this.twsApi = twsApi;
        this.stockContract = stockContract;
        this.bid = bid;
        this.divisor = divisor;
    }

    @Override
    public Optional<BigDecimal> price() {
        // must be new instance because of inner Future object which cannot be reused
        var handler = new MarketDataHandler(bid);
        twsApi.setMarketDataType(MarketDataType.DELAYED);
        twsApi.requestTopMarketData(stockContract, Collections.emptyList(), false, false, handler);
        try {
            return handler.price().map(price -> price.divide(BigDecimal.valueOf(divisor)));
        } finally {
            twsApi.cancelTopMarketData(handler);
        }
    }
}
