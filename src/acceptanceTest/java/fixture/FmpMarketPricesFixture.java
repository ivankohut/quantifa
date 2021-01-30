package fixture;

import lombok.Setter;
import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.Concatenated;
import org.cactoos.text.Joined;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Setter
public class FmpMarketPricesFixture {

    private static Text json;

    private final Map<String, BigDecimal> prices = new HashMap<>();

    private String symbol;
    private BigDecimal price;

    public static Text getJson() {
        return json;
    }

    private static void setJson(Text json) {
        FmpMarketPricesFixture.json = json;
    }

    public void execute() {
        prices.put(symbol, price);
    }

    public void endTable() {
        setJson(new Concatenated(() -> "[", new Joined(",", new Mapped<>(entry -> """
                {"symbol": "%s", "price": %s}""".formatted(entry.getKey(), entry.getValue().toPlainString()), prices.entrySet())), () -> "]"));
    }
}
