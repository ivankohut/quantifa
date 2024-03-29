package fixture;

import lombok.Setter;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.Unchecked;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class BookValueFixture {

    private ReportedAmount result;

    public void execute() {
        CacheUtils.clear();
        var stockContract = new SimpleStockContract("exchange", "symbol", "currency");
        result = new FixtureApplication(
                new FakeTwsApi(
                        stockContract,
                        new ReportFinancialStatementsXml(
                                new Mapped<>(
                                        entry -> new PeriodsXml(
                                                entry.getKey(),
                                                new Mapped<>(
                                                        bookValue -> new FiscalPeriodXml(
                                                                bookValue.getKey(),
                                                                new BalanceSheetXml(bookValue.getValue())
                                                        ),
                                                        entry.getValue()
                                                )
                                        ),
                                        BalanceSheetsWithBookValue.getValues()
                                )
                        )
                ),
                stockContract
        ).bookValue();
    }

    public BigDecimal value() {
        return new Unchecked<>(result.value()).value();
    }

    public LocalDate date() {
        return result.date();
    }
}
