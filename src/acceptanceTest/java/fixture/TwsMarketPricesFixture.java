package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Setter
public class TwsMarketPricesFixture {

    private String exchange;
    private String symbol;
    private String currency;
    private TickType tickType;
    private BigDecimal price;

    public void beforeAll() {
        TwsMarketPriceFixture.prices.clear();
    }

    public void execute() {
        TwsMarketPriceFixture.prices.merge(
                new SimpleStockContract(exchange, symbol, currency),
                new HashMap<>(Map.of(tickType, price)),
                (result, item) -> {
                    result.putAll(item);
                    return result;
                }
        );
    }
}
