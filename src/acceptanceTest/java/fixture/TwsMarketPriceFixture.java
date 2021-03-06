package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.StockContract;
import sk.ivankohut.quantifa.utils.ContentOfUriTest;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Setter
public class TwsMarketPriceFixture {

    private String exchange;
    private String symbol;
    private String currency;
    private int priceDivisor;

    static final Map<StockContract, Map<TickType, BigDecimal>> prices = new HashMap<>();

    public BigDecimal price() {
        CacheUtils.clear();
        var stockContract = new SimpleStockContract(exchange, symbol, currency);
        var fundamentalsStockContract = new SimpleStockContract("exchange", "symbol", "currency");
        var fmpApiKey = "fmpApiKey";
        var reportingCurrency = "reportingCurrency";
        return new Application(
                new FakeTwsApi(
                        prices,
                        fundamentalsStockContract,
                        new ReportFinancialStatementsXml(reportingCurrency)
                ),
                Clock.systemDefaultZone(),
                fundamentalsStockContract,
                CacheUtils.DIRECTORY,
                new SimplePriceRequest(stockContract, priceDivisor),
                ContentOfUriTest.createHttpClient(
                        "https://financialmodelingprep.com/api/v3/fx?apikey=" + fmpApiKey,
                        Duration.ofSeconds(15),
                        200,
                        new FmpExchangeRatesJson(reportingCurrency, currency, BigDecimal.ONE)
                ),
                fmpApiKey,
                ""

        ).price();
    }
}
