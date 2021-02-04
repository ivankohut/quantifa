package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class AvMarketPriceOfStock implements MarketPrice {

    private final Text json;

    @Override
    public Optional<BigDecimal> price() {
        var globalQuote = new JSONObject(new UncheckedText(json).asString()).getJSONObject("Global Quote");
        var key = "05. price";
        if (globalQuote.has(key)) {
            return Optional.of(globalQuote.getBigDecimal(key));
        }
        return Optional.empty();
    }
}
