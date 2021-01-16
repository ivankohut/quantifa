package fixture;

import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.StockContract;
import sk.ivankohut.quantifa.TwsApi;

import java.time.Clock;

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
        super(twsApi, clock, stockContract, StoredStatementsFixture.CACHE_DIRECTORY, 1);
    }

    public FixtureApplication(FakeTwsApi twsApi, StockContract stockContract, int priceDivisor) {
        super(twsApi, Clock.systemDefaultZone(), stockContract, StoredStatementsFixture.CACHE_DIRECTORY, priceDivisor);
    }
}
