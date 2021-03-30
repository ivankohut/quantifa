package sk.ivankohut.quantifa;

import org.cactoos.Scalar;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.Constant;
import org.cactoos.scalar.Reduced;
import org.cactoos.scalar.ScalarEnvelope;
import sk.ivankohut.quantifa.utils.StickyFirstOrFail;

import java.math.BigDecimal;
import java.util.Comparator;

public class TrailingTwelveMonths extends ScalarEnvelope<BigDecimal> {

    public TrailingTwelveMonths(Iterable<ReportedAmount> amounts) {
        super(create(amounts));
    }

    private static Scalar<BigDecimal> create(Iterable<ReportedAmount> amounts) {
        var threshold = new org.cactoos.scalar.Mapped<>(
                amount -> amount.date().minusYears(1).plusDays(10),
                new StickyFirstOrFail<>(
                        new Sorted<>(
                                Comparator.comparing(ReportedAmount::date).reversed(),
                                amounts
                        ),
                        "Missing amount."
                )
        );
        return new Reduced<>(
                BigDecimal::add,
                new Mapped<>(
                        amount -> new Constant<>(amount.value()),
                        new Filtered<>(amount -> amount.date().isAfter(threshold.value()), amounts)
                )
        );
    }
}
