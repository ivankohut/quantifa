package sk.ivankohut.quantifa;

import org.cactoos.Scalar;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FinancialStatement {

    LocalDate date();

    Scalar<BigDecimal> value(String name);
}
