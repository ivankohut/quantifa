package fixture;

import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.PriceRequest;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.StockContract;
import sk.ivankohut.quantifa.TwsApi;
import sk.ivankohut.quantifa.utils.ContentOfUriTest;

import java.net.http.HttpClient;
import java.time.Clock;
import java.time.Duration;

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
        super(twsApi, clock, stockContract, CacheUtils.DIRECTORY, new SimplePriceRequest(stockContract), mock(HttpClient.class));
    }

    public FixtureApplication(FakeTwsApi twsApi, StockContract stockContract, int priceDivisor) {
        super(
                twsApi,
                Clock.systemDefaultZone(),
                stockContract,
                CacheUtils.DIRECTORY,
                new SimplePriceRequest(stockContract, priceDivisor),
                mock(HttpClient.class)
        );
    }

    public FixtureApplication(
            Clock clock,
            PriceRequest priceRequest,
            String uri,
            String httpResponse
    ) {
        super(
                null, clock, new SimpleStockContract("", "", ""), CacheUtils.DIRECTORY, priceRequest,
                ContentOfUriTest.createHttpClient(uri, Duration.ofSeconds(15), 200, httpResponse)
        );
    }
}
