package sk.ivankohut.quantifa.utils;

import org.cactoos.Scalar;
import org.cactoos.iterator.IteratorOf;
import org.cactoos.scalar.Unchecked;

import java.time.format.DateTimeParseException;
import java.util.Iterator;

public class DateTimeParseExceptionToIterable<T> implements Iterable<T> {

    private final Scalar<T> scalar;

    public DateTimeParseExceptionToIterable(Scalar<T> scalar) {
        this.scalar = scalar;
    }

    @Override
    public Iterator<T> iterator() {
        try {
            return new IteratorOf<>(new Unchecked<>(scalar).value());
        } catch (DateTimeParseException e) {
            return new IteratorOf<>();
        }
    }
}
