package sk.ivankohut.quantifa;

import org.cactoos.Text;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.UncheckedText;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Optional;

public class FmpMarketPriceOfStock implements MarketPrice {

    private final Text prices;
    private final String symbol;

    public FmpMarketPriceOfStock(Text prices, String symbol) {
        this.prices = prices;
        this.symbol = symbol;
    }

    // probably bug in SonarQube rule
    @SuppressWarnings({ "rawtypes", "unchecked", "java:S2293" })
    @Override
    public Optional<BigDecimal> price() {
        return new Unchecked<>(new FirstOf<Optional<BigDecimal>>(
                new Mapped<>(
                        o -> Optional.of(o.getBigDecimal("price")),
                        new Filtered<>(
                                o -> o.getString("symbol").equals(symbol),
                                (Iterable<JSONObject>) (Iterable) new JSONArray(new UncheckedText(prices).asString())
                        )
                ),
                Optional.empty()
        )).value();
    }
}
