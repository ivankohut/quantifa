package sk.ivankohut.quantifa.utils;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;

@RequiredArgsConstructor
public class DelayedScalar<T> implements Scalar<T> {

    private final Scalar<T> scalar;
    private final long delay;

    @Override
    @SuppressWarnings({ "PMD.AvoidThrowingRawExceptionTypes", "java:S112" })
    public T value() throws Exception {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return scalar.value();
    }
}
