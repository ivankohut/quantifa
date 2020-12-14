package fixture;

import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.BalanceSheet;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class NyseCatBookValueFixture implements BalanceSheet {

    private String exchange;
    private String symbol;
    private String currency;

    private BalanceSheet result;

    public void execute() {
        result = new Application(new FakeBookValueTwsApi(new SimpleStockContract(exchange, symbol, currency), BalanceSheetsWithBookValue.getValues()));
    }

    @Override
    public BigDecimal bookValue() {
        return result.bookValue();
    }

    @Override
    public LocalDate date() {
        return result.date();
    }
}
