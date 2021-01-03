package sk.ivankohut.quantifa.decimal;

import org.cactoos.Scalar;
import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Rounded extends ScalarEnvelope<BigDecimal> {

    public Rounded(Scalar<BigDecimal> scalar) {
        super(() -> scalar.value().setScale(4, RoundingMode.HALF_UP));
    }
}
