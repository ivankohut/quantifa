package fixture;

import lombok.Setter;
import org.cactoos.text.UncheckedText;
import sk.ivankohut.quantifa.CachedFinancialStatementsTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class FmpMarketPriceFixture {

    private static final LocalDate DATE = LocalDate.of(2020, 1, 17);

    private String symbol;
    private int priceDivisor;

    public BigDecimal price() {
        return new FixtureApplication(
                CachedFinancialStatementsTest.clockFixedOn(DATE),
                new SimplePriceRequest("FMP", symbol, priceDivisor),
                "https://financialmodelingprep.com/api/v3/stock/list?apikey=fmpApiKey",
                new UncheckedText(FmpMarketPricesFixture.getJson()).asString(),
                "fmpApiKey",
                ""
        ).price();
    }
}
