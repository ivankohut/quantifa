package fixture;

import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class BookValueFixture implements ReportedAmount {

    private ReportedAmount result;

    public void execute() {
        StoredStatementsFixture.clearCache();
        var stockContract = new SimpleStockContract("exchange", "symbol", "currency");
        result = new Application(new FakeBookValueTwsApi(stockContract, BalanceSheetsWithBookValue.getValues()), stockContract).bookValue();
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
