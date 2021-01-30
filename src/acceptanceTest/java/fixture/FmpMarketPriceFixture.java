package fixture;

import lombok.Setter;
import org.cactoos.list.ListOf;
import org.cactoos.text.UncheckedText;
import sk.ivankohut.quantifa.CachedFinancialStatementsTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class FmpMarketPriceFixture {

    private static final LocalDate DATE = LocalDate.of(2020, 1, 17);

    private String symbol;
    private int priceDivisor;

    public void beginTable() {
        CacheUtils.resetWith(
                () -> "prices/fmp",
                new ListOf<>(DATE),
                ".json",
                new UncheckedText(FmpMarketPricesFixture.getJson()).asString()
        );
    }

    public BigDecimal price() {
        return new FixtureApplication(CachedFinancialStatementsTest.clockFixedOn(DATE), new SimplePriceRequest(symbol, priceDivisor)).price();
    }
}
