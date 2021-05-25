package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class FinancialStatementAmount implements ReportedAmount {

    private final FinancialStatement statement;
    private final String lineItem;

    @Override
    public LocalDate date() {
        return statement.date();
    }

    @Override
    public Scalar<BigDecimal> value() {
        return statement.value(lineItem);
    }
}
