package fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;
import org.cactoos.text.Concatenated;
import org.cactoos.text.Joined;
import org.cactoos.text.TextEnvelope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FmpCurrencyExchangeRatesFixture {

    private String from;
    @SuppressWarnings("PMD.ShortVariable")
    private String to;
    private BigDecimal rate;

    private static final List<FmpCurrencyExchangeRatesFixture> rates = new ArrayList<>();

    public static Text getJson() {
        return new FmpExchangeRatesJson(rates);
    }

    public void execute() {
        rates.add(new FmpCurrencyExchangeRatesFixture(from, to, rate));
    }
}

class FmpExchangeRatesJson extends TextEnvelope {

    public FmpExchangeRatesJson(Iterable<FmpCurrencyExchangeRatesFixture> rates) {
        super(new Concatenated(() -> "[", new Joined(",", new Mapped<>(rate -> """
                {"ticker": "%s/%s", "bid": %s}""".formatted(rate.getFrom(), rate.getTo(), rate.getRate().toPlainString()), rates)), () -> "]"));
    }

    @SuppressWarnings("PMD.ShortVariable")
    public FmpExchangeRatesJson(String from, String to, BigDecimal rate) {
        this(new ListOf<>(new FmpCurrencyExchangeRatesFixture(from, to, rate)));
    }
}
