package fixture;

import org.cactoos.iterable.Mapped;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.StockContract;

import java.util.List;
import java.util.Map;

public class FakeEpsTwsApi extends FakeTwsApi {

    public FakeEpsTwsApi(StockContract stockContract, List<ReportedAmount> dateAndAmount, String period) {
        super(
                stockContract,
                new ReportFinancialStatementsXml(
                        new PeriodsXml(
                                period,
                                new Mapped<>(
                                        eps -> new FiscalPeriodXml(eps.date(), new IncomeStatementXml(Map.of("SDBF", eps.value().value()))),
                                        dateAndAmount
                                )
                        )
                )
        );
    }
}
