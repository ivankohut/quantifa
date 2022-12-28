package sk.ivankohut.quantifa.utils;

import org.cactoos.Scalar;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Mapped;
import org.cactoos.number.SumOf;
import org.cactoos.scalar.ScalarEnvelope;

public class SumOfLong extends ScalarEnvelope<Long> {

    public SumOfLong(Iterable<Scalar<Long>> values) {
        super(() -> new SumOf(new Mapped<>(Scalar::value, values)).longValue());
    }

    @SafeVarargs
    public SumOfLong(Scalar<Long>... scalars) {
        this(new IterableOf<>(scalars));
    }
}
