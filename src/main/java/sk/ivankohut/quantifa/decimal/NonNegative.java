package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;

public class NonNegative extends ScalarEnvelope<BigDecimal> {

    public NonNegative(Scalar<BigDecimal> decimal) {
        super(() -> decimal.value().max(BigDecimal.ZERO));
    }
}
