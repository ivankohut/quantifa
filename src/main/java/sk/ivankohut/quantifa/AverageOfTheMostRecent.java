package sk.ivankohut.quantifa;

import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.iterable.TailOf;
import org.cactoos.scalar.AvgOf;
import sk.ivankohut.quantifa.decimal.DecimalOf;

import java.util.Comparator;

public class AverageOfTheMostRecent extends DecimalOf {

    // necessary because the parameter of the ctor of AvgOf is defined as Iterable<Scalar<Number>> instead of Iterable<Scalar<? extends Number>>
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public AverageOfTheMostRecent(Iterable<? extends ReportedAmount> amounts, int numberOfItems) {
        super(new AvgOf(
                (Iterable) new TailOf<>(
                        numberOfItems,
                        new Mapped<>(
                                ReportedAmount::value,
                                new Sorted<>(
                                        Comparator.comparing(ReportedAmount::date),
                                        amounts
                                )
                        )
                )
        ));
    }
}
