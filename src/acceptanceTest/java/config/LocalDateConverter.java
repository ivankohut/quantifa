package config;

import fitnesse.slim.Converter;

import java.time.LocalDate;

public class LocalDateConverter implements Converter<LocalDate> {

    @Override
    public LocalDate fromString(String value) {
        if (value != null && !value.isEmpty()) {
            return LocalDate.parse(value);
        }
        return null;
    }

    @Override
    public String toString(LocalDate value) {
        return value == null ? "" : value.toString();
    }
}
