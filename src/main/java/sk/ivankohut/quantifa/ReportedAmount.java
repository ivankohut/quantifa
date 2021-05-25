package sk.ivankohut.quantifa;

import org.cactoos.Scalar;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReportedAmount {

    LocalDate date();

    Scalar<BigDecimal> value();
}
