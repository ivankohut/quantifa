package fixture;

import lombok.Setter;
import sk.ivankohut.quantifa.CachedFinancialStatementsTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class YfMarketPriceFixture {

    private static final LocalDate DATE = LocalDate.of(2020, 1, 17);

    private String symbol;
    private int priceDivisor;

    public BigDecimal price() {
        return new FixtureApplication(
                CachedFinancialStatementsTest.clockFixedOn(DATE),
                new SimplePriceRequest("YF", symbol, priceDivisor),
                "https://finance.yahoo.com/quote/%s".formatted(symbol),
                YfMarketPricesFixture.priceOf(symbol)
                        .map("""
                                  start root.App.main = {"context":{"dispatcher":{"stores":{"QuoteSummaryStore":{"financialData":{
                                  "currentPrice": {"raw": %s, "fmt": "1.11" }
                                }}}}}}; end"""::formatted)
                        .orElse(""),
                "",
                ""
        ).price();
    }
}
