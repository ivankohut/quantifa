package sk.ivankohut.quantifa.utils;

import org.cactoos.scalar.ScalarEnvelope;

public class IsNotEmpty extends ScalarEnvelope<Boolean> {

    public IsNotEmpty(Iterable<?> iterable) {
        super(() -> iterable.iterator().hasNext());
    }
}
