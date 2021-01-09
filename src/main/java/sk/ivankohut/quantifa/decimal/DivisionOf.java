package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;
import java.math.MathContext;

public class DivisionOf extends ScalarEnvelope<BigDecimal> {

    public DivisionOf(Scalar<BigDecimal> dividend, Scalar<BigDecimal> divisor, BigDecimal ifDivisorZero) {
        super(() -> {
            var divisorValue = divisor.value();
            if (divisorValue.signum() == 0) {
                return ifDivisorZero;
            }
            return dividend.value().divide(divisorValue, MathContext.DECIMAL128);
        });
    }

    public DivisionOf(BigDecimal dividend, BigDecimal divisor, BigDecimal ifDivisorZero) {
        this(dividend, () -> divisor, ifDivisorZero);
    }

    public DivisionOf(BigDecimal dividend, Scalar<BigDecimal> divisor, BigDecimal ifDivisorZero) {
        this(() -> dividend, divisor, ifDivisorZero);
    }
}
