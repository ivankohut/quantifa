package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Setter
public class GrahamNumberFixture {

    private BigDecimal earnings;
    private BigDecimal bookValue;
    private BigDecimal price;

    private Application application;

    public void execute() {
        StoredStatementsFixture.clearCache();
        var stockContract = new SimpleStockContract("exchange", "symbol", "currency");
        this.application = new Application(
                new FakeTwsApi(
                        Map.of(stockContract, Map.of(TickType.DELAYED_BID, price)),
                        stockContract,
                        new ReportFinancialStatementsXml(
                                new PeriodsXml("Annual", new FiscalPeriodXml(
                                        new FinancialStatementXml("INC", LocalDate.now(), Map.of("VDES", earnings))
                                )),
                                new PeriodsXml("Interim", new FiscalPeriodXml(
                                        new FinancialStatementXml("BAL", LocalDate.now(), Map.of("QTLE", bookValue, "QTCO", BigDecimal.ONE))
                                ))
                        )
                ),
                stockContract
        );
    }

    public BigDecimal grahamNumber() {
        return application.grahamNumber();
    }

    public BigDecimal grahamRatio() {
        return application.grahamRatio();
    }
}
