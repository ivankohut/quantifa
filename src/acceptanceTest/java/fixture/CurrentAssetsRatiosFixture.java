package fixture;

import org.cactoos.list.Joined;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CurrentAssetsRatiosFixture {

    private Optional<BigDecimal> currentAssets;
    private Optional<BigDecimal> currentLiabilities;
    private Optional<BigDecimal> longTermDebt;

    private Application application;

    public void setCurrentAssets(BigDecimal currentAssets) {
        this.currentAssets = Optional.ofNullable(currentAssets);
    }

    public void setCurrentLiabilities(BigDecimal currentLiabilities) {
        this.currentLiabilities = Optional.ofNullable(currentLiabilities);
    }

    public void setLongTermDebt(BigDecimal longTermDebt) {
        this.longTermDebt = Optional.ofNullable(longTermDebt);
    }

    @SuppressWarnings("unchecked")
    public void execute() {
        CacheUtils.clear();
        var stockContract = new SimpleStockContract("exchange", "symbol", "currency");
        this.application = new FixtureApplication(
                new FakeTwsApi(
                        stockContract,
                        new ReportFinancialStatementsXml(
                                new PeriodsXml("Interim", new FiscalPeriodXml(
                                        new FinancialStatementXml("BAL", LocalDate.now(), Map.ofEntries(new Joined<>(
                                                entryList("ATCA", currentAssets),
                                                entryList("LTCL", currentLiabilities),
                                                entryList("LTTD", longTermDebt)
                                        ).toArray(new Map.Entry[] {})))
                                ))
                        )
                ),
                stockContract
        );
    }

    private static List<Map.Entry<String, BigDecimal>> entryList(String key, Optional<BigDecimal> maybeValue) {
        return maybeValue.map(value -> List.of(Map.entry(key, value))).orElse(List.of());
    }

    public BigDecimal currentRatio() {
        return application.currentRatio();
    }

    public BigDecimal netCurrentAssetsToLongTermDebt() {
        return application.netCurrentAssetsToLongTermDebtRatio();
    }
}
