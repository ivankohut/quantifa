package fixture;

import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Setter
public class YfMarketPricesFixture {

    private static final Map<String, BigDecimal> prices = new HashMap<>();

    private String symbol;
    private BigDecimal price;

    public YfMarketPricesFixture() {
        prices.clear();
    }

    public void execute() {
        prices.put(symbol, price);
    }

    public static Optional<BigDecimal> priceOf(String symbol) {
        return Optional.ofNullable(prices.get(symbol));
    }
}
