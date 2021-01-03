package fixture;

import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Setter
public class CurrentAssetsRatiosFixture {

    private BigDecimal currentAssets;
    private BigDecimal currentLiabilities;
    private BigDecimal longTermDebt;

    private Application application;

    public void execute() {
        StoredStatementsFixture.clearCache();
        var stockContract = new SimpleStockContract("exchange", "symbol", "currency");
        this.application = new Application(
                new FakeTwsApi(
                        stockContract,
                        new ReportFinancialStatementsXml(
                                new PeriodsXml("Interim", new FiscalPeriodXml(
                                        new FinancialStatementXml("BAL", LocalDate.now(), Map.of(
                                                "ATCA", currentAssets,
                                                "LTCL", currentLiabilities,
                                                "LTTD", longTermDebt
                                        ))
                                ))
                        )
                ),
                stockContract
        );
    }

    public BigDecimal currentRatio() {
        return application.currentRatio();
    }

    public BigDecimal netCurrentAssetsToLongTermDebt() {
        return application.netCurrentAssetsToLongTermDebtRatio();
    }
}
