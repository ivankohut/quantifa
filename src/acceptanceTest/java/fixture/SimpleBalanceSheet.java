package fixture;

import lombok.RequiredArgsConstructor;
import sk.ivankohut.quantifa.BalanceSheet;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class SimpleBalanceSheet implements BalanceSheet {

    private final LocalDate date;
    private final BigDecimal bookValue;

    @Override
    public LocalDate date() {
        return date;
    }

    @Override
    public BigDecimal bookValue() {
        return bookValue;
    }
}
