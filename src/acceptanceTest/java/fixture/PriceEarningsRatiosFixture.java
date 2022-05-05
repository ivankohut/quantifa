package fixture;

import lombok.Setter;
import sk.ivankohut.quantifa.Application;

import java.math.BigDecimal;

@Setter
@SuppressWarnings("java:S100")
public class PriceEarningsRatiosFixture {

    private BigDecimal earningsTTM;
    private BigDecimal earningsAverage;
    private BigDecimal price;

    private Application application;

    public void execute() {
        CacheUtils.clear();
        this.application = GrahamNumberFixture.createApplication(earningsTTM, earningsAverage, BigDecimal.ZERO, price);
    }

    public BigDecimal PETTM() {
        return application.peTtm();
    }

    public BigDecimal PEAverage() {
        return application.peAverage();
    }
}
