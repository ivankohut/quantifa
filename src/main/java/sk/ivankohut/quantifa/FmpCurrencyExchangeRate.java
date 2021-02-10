package sk.ivankohut.quantifa;

import org.cactoos.Text;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.Equals;
import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.UncheckedText;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@SuppressWarnings("PMD.ShortVariable")
public class FmpCurrencyExchangeRate implements MarketPrice {

    private final Text rates;
    private final String from;
    private final String to;

    public FmpCurrencyExchangeRate(Text rates, String from, String to) {
        this.rates = rates;
        this.from = from;
        this.to = to;
    }

    @Override
    // probably bug in SonarQube rule
    @SuppressWarnings({ "rawtypes", "unchecked", "java:S2293", "java:S1905" })
    public Optional<BigDecimal> price() {
        return new Unchecked<>(new Ternary<Optional<BigDecimal>>(
                new Equals(() -> from, () -> to),
                () -> Optional.of(BigDecimal.ONE),
                () -> {
                    var rateObjects = (Iterable) new JSONArray(new UncheckedText(rates).asString());
                    return new FmpCurrencyExchangeRateBid(rateObjects, from, to).price()
                            .or(() -> new FmpCurrencyExchangeRateBid(rateObjects, to, from).price()
                                    .map(p -> BigDecimal.ONE.divide(p, 6, RoundingMode.HALF_UP)));
                }
        )).value();
    }
}

@SuppressWarnings("PMD.ShortVariable")
class FmpCurrencyExchangeRateBid implements MarketPrice {

    private final Iterable<JSONObject> rates;
    private final String from;
    private final String to;

    public FmpCurrencyExchangeRateBid(Iterable<JSONObject> rates, String from, String to) {
        this.rates = rates;
        this.from = from;
        this.to = to;
    }

    @Override
    // probably bug in SonarQube rule
    @SuppressWarnings("java:S2293")
    public Optional<BigDecimal> price() {
        return new Unchecked<>(new FirstOf<Optional<BigDecimal>>(
                new Mapped<>(
                        o -> Optional.of(o.getBigDecimal("bid")),
                        new Filtered<>(
                                o -> {
                                    var ticker = o.getString("ticker");
                                    return ticker.equals(from + "/" + to);
                                },
                                rates
                        )
                ),
                Optional.empty()
        )).value();
    }
}
