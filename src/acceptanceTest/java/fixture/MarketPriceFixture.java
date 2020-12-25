package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.StockContract;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Setter
public class MarketPriceFixture {

    private String exchange;
    private String symbol;
    private String currency;

    static final Map<StockContract, Map<TickType, BigDecimal>> prices = new HashMap<>();

    public BigDecimal price() {
        var stockContract = new SimpleStockContract(exchange, symbol, currency);
        return new Application(new FakePriceMarketDataTwsApi(prices), stockContract).price().price().orElse(BigDecimal.ZERO);
    }
}
