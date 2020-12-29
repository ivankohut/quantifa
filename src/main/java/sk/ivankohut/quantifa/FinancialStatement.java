package sk.ivankohut.quantifa;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FinancialStatement {

    LocalDate date();

    BigDecimal value(String name);
}
