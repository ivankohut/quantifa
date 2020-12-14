package config;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InitializeSystem {

    public InitializeSystem() {
        fitnesse.slim.converters.ConverterRegistry.addConverter(BigDecimal.class, new BigDecimalConverter());
        fitnesse.slim.converters.ConverterRegistry.addConverter(LocalDate.class, new LocalDateConverter());
    }
}
