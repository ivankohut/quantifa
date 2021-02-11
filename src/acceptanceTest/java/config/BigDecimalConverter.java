package config;

import fitnesse.slim.Converter;

import java.math.BigDecimal;

public class BigDecimalConverter implements Converter<BigDecimal> {

    @Override
    public BigDecimal fromString(String value) {
        if (value != null && !value.isEmpty()) {
            return new BigDecimal(value);
        }
        return null;
    }

    @Override
    public String toString(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }
}
