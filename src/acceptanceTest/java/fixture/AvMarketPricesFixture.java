package fixture;

import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Setter
public class AvMarketPricesFixture {

    private static final Map<String, BigDecimal> prices = new HashMap<>();

    private String symbol;
    private BigDecimal price;

    public AvMarketPricesFixture() {
        prices.clear();
    }

    public void execute() {
        prices.put(symbol, price);
    }

    public static Map<String, BigDecimal> getPrices() {
        return prices;
    }
}
