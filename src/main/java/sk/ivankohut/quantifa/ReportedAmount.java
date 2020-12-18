package sk.ivankohut.quantifa;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReportedAmount {

    LocalDate date();

    BigDecimal value();
}
