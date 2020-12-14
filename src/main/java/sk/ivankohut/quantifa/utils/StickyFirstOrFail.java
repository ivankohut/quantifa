package sk.ivankohut.quantifa.utils;

import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.ScalarEnvelope;
import org.cactoos.scalar.Sticky;
import sk.ivankohut.quantifa.ApplicationException;

public class StickyFirstOrFail<T> extends ScalarEnvelope<T> {

    // probably bug in SonarQube rule
    @SuppressWarnings("java:S2293")
    public StickyFirstOrFail(Iterable<T> items, String missingMessage) {
        super(new Sticky<>(new FirstOf<T>(
                items,
                () -> {
                    throw new ApplicationException(missingMessage);
                }
        )));
    }
}
