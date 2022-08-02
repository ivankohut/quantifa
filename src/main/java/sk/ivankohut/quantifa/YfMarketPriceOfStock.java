package sk.ivankohut.quantifa;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.text.UncheckedText;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class YfMarketPriceOfStock implements MarketPrice {

    private static final Pattern PATTERN = Pattern.compile(".*root\\.App\\.main = (.*);.*", Pattern.MULTILINE + Pattern.DOTALL);

    private final Text html;

    @Override
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public Optional<BigDecimal> price() {
        var matcher = PATTERN.matcher(new UncheckedText(html).asString());
        if (matcher.matches()) {
            try {
                var documentContext = JsonPath.parse(matcher.group(1), Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build());
                var rawPrice = documentContext.read("$.context.dispatcher.stores.QuoteSummaryStore.financialData.currentPrice.raw");
                if (rawPrice instanceof Number rawPriceNumber) {
                    return Optional.of(BigDecimal.valueOf(rawPriceNumber.doubleValue()));
                }
            } catch (InvalidJsonException e) {
                // empty
            }
        }
        return Optional.empty();
    }
}
