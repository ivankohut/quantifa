package fixture;

import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class NyseCatBookValueFixture implements ReportedAmount {

    private String exchange;
    private String symbol;
    private String currency;

    private ReportedAmount result;

    public void execute() {
        StoredStatementsFixture.clearCache();
        result = new Application(
                new FakeBookValueTwsApi(new SimpleStockContract(exchange, symbol, currency), BalanceSheetsWithBookValue.getValues())
        ).bookValue();
    }

    @Override
    public BigDecimal value() {
        return result.value();
    }

    @Override
    public LocalDate date() {
        return result.date();
    }
}
