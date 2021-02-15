package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;

public class Negated extends ScalarEnvelope<BigDecimal> {

    public Negated(Scalar<BigDecimal> decimal) {
        super(() -> decimal.value().negate());
    }
}
