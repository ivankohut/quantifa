package sk.ivankohut.quantifa.utils;

import org.cactoos.Text;
import org.cactoos.scalar.ScalarEnvelope;

/**
 * Integer length of text (type of {@link org.cactoos.scalar.LengthOf} is double).
 */
public class LengthOf extends ScalarEnvelope<Integer> {

    public LengthOf(Text text) {
        super(() -> text.asString().length());
    }
}
