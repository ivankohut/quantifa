package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class SimpleReportedAmount implements ReportedAmount {

    private final LocalDate date;
    private final BigDecimal value;

    @Override
    public LocalDate date() {
        return date;
    }

    @Override
    public Scalar<BigDecimal> value() {
        return () -> value;
    }
}
