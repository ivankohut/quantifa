package fixture;

import org.cactoos.text.UncheckedText;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.PriceRequest;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.StockContract;
import sk.ivankohut.quantifa.TwsApi;
import sk.ivankohut.quantifa.utils.ContentOfUriTest;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.time.Clock;
import java.time.Duration;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class FixtureApplication extends Application {

    public FixtureApplication(
            TwsApi twsApi,
            StockContract stockContract
    ) {
        this(twsApi, Clock.systemDefaultZone(), stockContract);
    }

    public FixtureApplication(
            TwsApi twsApi,
            Clock clock,
            StockContract stockContract
    ) {
        super(twsApi, clock, stockContract, CacheUtils.DIRECTORY, new SimplePriceRequest(stockContract), mock(HttpClient.class), "", "");
    }

    public FixtureApplication(
            Clock clock,
            PriceRequest priceRequest,
            String uri,
            String httpResponse,
            String fmpApiKey,
            String avApiKey
    ) {
        super(
                new FakeTwsApi(
                        new SimpleStockContract("exchange", "symbol", "currency"),
                        new ReportFinancialStatementsXml("reportingCurrency")
                ),
                clock,
                new SimpleStockContract("exchange", "symbol", "currency"),
                CacheUtils.DIRECTORY,
                priceRequest,
                ContentOfUriTest.createHttpClient(
                        Map.of(
                                uri,
                                httpResponse,
                                "https://financialmodelingprep.com/api/v3/fx?apikey=" + fmpApiKey,
                                new UncheckedText(new FmpExchangeRatesJson("reportingCurrency", priceRequest.currency(), BigDecimal.ONE)).asString()
                        ),
                        Duration.ofSeconds(15),
                        200
                ),
                fmpApiKey,
                avApiKey
        );
    }
}
