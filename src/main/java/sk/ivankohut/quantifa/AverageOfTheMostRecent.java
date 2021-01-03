package sk.ivankohut.quantifa;

import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.iterable.TailOf;
import org.cactoos.scalar.AvgOf;
import org.cactoos.scalar.Constant;
import sk.ivankohut.quantifa.decimal.DecimalOf;

import java.util.Comparator;

public class AverageOfTheMostRecent extends DecimalOf {

    public AverageOfTheMostRecent(Iterable<ReportedAmount> amounts, int numberOfItems) {
        super(new AvgOf(
                new TailOf<>(
                        numberOfItems,
                        new Mapped<>(
                                reportedAmount -> new Constant<>(reportedAmount.value()),
                                new Sorted<>(
                                        Comparator.comparing(ReportedAmount::date),
                                        amounts
                                )
                        )
                )
        ));
    }
}
