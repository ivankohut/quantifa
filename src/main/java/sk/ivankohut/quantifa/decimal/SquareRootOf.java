package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;
import java.math.MathContext;

public class SquareRootOf extends ScalarEnvelope<BigDecimal> {

    public SquareRootOf(Scalar<BigDecimal> decimal) {
        super(() -> decimal.value().sqrt(MathContext.DECIMAL128));
    }

    public SquareRootOf(BigDecimal decimal) {
        this(() -> decimal);
    }
}
