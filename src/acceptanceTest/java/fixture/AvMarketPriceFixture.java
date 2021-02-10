package fixture;

import lombok.Setter;
import sk.ivankohut.quantifa.CachedFinancialStatementsTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Setter
public class AvMarketPriceFixture {

    private static final LocalDate DATE = LocalDate.of(2020, 1, 17);

    private String symbol;
    private int priceDivisor;

    public BigDecimal price() {
        return new FixtureApplication(
                CachedFinancialStatementsTest.clockFixedOn(DATE),
                new SimplePriceRequest("AV", symbol, priceDivisor),
                "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=avApiKey".formatted(symbol),
                Optional.ofNullable(AvMarketPricesFixture.getPrices().get(symbol))
                        .map("{\"Global Quote\": { \"05. price\": \"%s\"}}"::formatted)
                        .orElse("{\"Global Quote\": {}}"),
                "",
                "avApiKey"
        ).price();
    }
}
