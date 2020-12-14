package sk.ivankohut.quantifa;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BalanceSheet {

    LocalDate date();

    BigDecimal bookValue();
}
