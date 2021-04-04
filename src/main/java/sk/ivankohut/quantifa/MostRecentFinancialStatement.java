package sk.ivankohut.quantifa;

import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MostRecentFinancialStatement implements FinancialStatement {

    private final Unchecked<FinancialStatement> statement;

    // probably bug in SonarQube rule
    @SuppressWarnings("java:S2293")
    public MostRecentFinancialStatement(Iterable<FinancialStatement> statements) {
        this.statement = new Unchecked<>(new Sticky<>(new FirstOf<FinancialStatement>(
                new Sorted<>(
                        (e1, e2) -> e2.date().compareTo(e1.date()),
                        statements
                ),
                () -> {
                    throw new IllegalArgumentException("No any financial statement available - not possible to choose the most recent one.");
                }
        )));
    }

    @Override
    public LocalDate date() {
        return statement.value().date();
    }

    @Override
    public BigDecimal value(String name) {
        return statement.value().value(name);
    }
}
