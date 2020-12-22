package sk.ivankohut.quantifa.utils;

import lombok.RequiredArgsConstructor;
import org.cactoos.Proc;
import org.cactoos.Scalar;

/**
 * Performs given side-effect where providing value.
 */
@RequiredArgsConstructor
public class PeekedScalar<T> implements Scalar<T> {

    private final Scalar<T> scalar;
    private final Proc<T> proc;

    @Override
    public T value() throws Exception {
        var result = scalar.value();
        proc.exec(result);
        return result;
    }
}
