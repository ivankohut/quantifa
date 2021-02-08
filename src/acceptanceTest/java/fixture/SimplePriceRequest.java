package fixture;

import lombok.RequiredArgsConstructor;
import sk.ivankohut.quantifa.PriceRequest;
import sk.ivankohut.quantifa.StockContract;

@RequiredArgsConstructor
@SuppressWarnings("PMD.DataClass")
public class SimplePriceRequest implements PriceRequest {

    private final String source;
    private final String apiKey;
    private final String exchange;
    private final String symbol;
    private final String currency;
    private final int divisor;

    // TWS price request
    public SimplePriceRequest(StockContract stockContract, int priceDivisor) {
        this("TWS", "", stockContract.exchange(), stockContract.symbol(), stockContract.currency(), priceDivisor);
    }

    // TWS price request
    public SimplePriceRequest(StockContract stockContract) {
        this(stockContract, 1);
    }

    public SimplePriceRequest(String source, String apiKey, String symbol, int priceDivisor) {
        this(source, apiKey, "", symbol, "", priceDivisor);
    }

    @Override
    public String source() {
        return source;
    }

    @Override
    public String apiKey() {
        return apiKey;
    }

    @Override
    public String exchange() {
        return exchange;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public String currency() {
        return currency;
    }

    @Override
    public int divisor() {
        return divisor;
    }
}
