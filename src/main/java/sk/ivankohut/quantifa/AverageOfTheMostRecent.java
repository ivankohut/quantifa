package sk.ivankohut.quantifa;

import org.cactoos.Scalar;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.iterable.TailOf;
import org.cactoos.number.AvgOf;
import sk.ivankohut.quantifa.decimal.DecimalOf;

import java.util.Comparator;

public class AverageOfTheMostRecent extends DecimalOf {

    public AverageOfTheMostRecent(Iterable<? extends ReportedAmount> amounts, int numberOfItems) {
        super(new AvgOf(
                new Mapped<>(
                        Scalar::value,
                        new TailOf<>(
                                numberOfItems,
                                new Mapped<>(
                                        ReportedAmount::value,
                                        new Sorted<>(
                                                Comparator.comparing(ReportedAmount::date),
                                                amounts
                                        )
                                )
                        )
                )
        ));
    }
}
