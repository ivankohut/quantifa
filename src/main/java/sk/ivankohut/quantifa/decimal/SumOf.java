package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.Folded;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;

public class SumOf extends ScalarEnvelope<BigDecimal> {

    public SumOf(final Iterable<BigDecimal> addends) {
        super(() -> new Folded<>(BigDecimal.ZERO, BigDecimal::add, addends).value());
    }

    public SumOf(BigDecimal... addends) {
        this(new ListOf<>(addends));
    }

    @SafeVarargs
    public SumOf(Scalar<BigDecimal>... addends) {
        this(new Mapped<>(Scalar::value, new ListOf<>(addends)));
    }
}
