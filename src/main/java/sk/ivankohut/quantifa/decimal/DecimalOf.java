package sk.ivankohut.quantifa.decimal;

import org.cactoos.scalar.ScalarEnvelope;

import java.math.BigDecimal;

public class DecimalOf extends ScalarEnvelope<BigDecimal> {

    public DecimalOf(Number value) {
        super(() -> BigDecimal.valueOf(value.doubleValue()));
    }
}
