package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.utils.ContentOfUriTest;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.util.Map;

@Setter
public class MarketPriceInReportingCurrencyFixture {

    private BigDecimal priceFromPriceSource;
    private String currency;
    private String reportingCurrency;

    public BigDecimal price() {
        CacheUtils.clear();
        var stockContract = new SimpleStockContract("exchange", "symbol", currency);
        var fundamentalsStockContract = new SimpleStockContract("exchange", "symbol", "currency");
        var fmpApiKey = "fmpApiKey";
        return new Application(
                new FakeTwsApi(
                        Map.of(stockContract, Map.of(TickType.DELAYED_BID, priceFromPriceSource)),
                        fundamentalsStockContract,
                        new ReportFinancialStatementsXml(reportingCurrency)
                ),
                Clock.systemDefaultZone(),
                fundamentalsStockContract,
                CacheUtils.DIRECTORY,
                new SimplePriceRequest(stockContract, 1),
                ContentOfUriTest.createHttpClient(
                        "https://financialmodelingprep.com/api/v3/fx?apikey=" + fmpApiKey,
                        Duration.ofSeconds(15),
                        200,
                        FmpCurrencyExchangeRatesFixture.getJson()
                ),
                fmpApiKey,
                ""

        ).price();
    }
}
