package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;
import java.math.MathContext;

public class DivisionOf extends ScalarEnvelope<BigDecimal> {

    public DivisionOf(Scalar<BigDecimal> dividend, Scalar<BigDecimal> divisor) {
        super(() -> dividend.value().divide(divisor.value(), MathContext.DECIMAL128));
    }

    public DivisionOf(BigDecimal dividend, BigDecimal divisor) {
        this(dividend, () -> divisor);
    }

    public DivisionOf(BigDecimal dividend, Scalar<BigDecimal> divisor) {
        this(() -> dividend, divisor);
    }
}
