package sk.ivankohut.quantifa.utils;

import org.cactoos.Text;
import org.cactoos.scalar.ScalarEnvelope;

import java.time.LocalDate;

public class LocalDateOf extends ScalarEnvelope<LocalDate> {

    public LocalDateOf(Text text) {
        super(() -> LocalDate.parse(text.asString()));
    }
}
