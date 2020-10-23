package config;

import java.math.BigDecimal;

public class InitializeSystem {

    public InitializeSystem() {
        fitnesse.slim.converters.ConverterRegistry.addConverter(BigDecimal.class, new BigDecimalConverter());
    }
}
