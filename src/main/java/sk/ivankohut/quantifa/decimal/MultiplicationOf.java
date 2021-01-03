package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.iterable.IterableOf;
import org.cactoos.scalar.Folded;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;
import java.math.MathContext;

public class MultiplicationOf extends ScalarEnvelope<BigDecimal> {

    public MultiplicationOf(Iterable<Scalar<BigDecimal>> multiplicands) {
        super(new Folded<>(
                BigDecimal.ONE,
                (accumulator, value) -> accumulator.multiply(value.value(), MathContext.DECIMAL128),
                multiplicands
        ));
    }

    @SafeVarargs
    public MultiplicationOf(Scalar<BigDecimal>... src) {
        this(new IterableOf<>(src));
    }
}
