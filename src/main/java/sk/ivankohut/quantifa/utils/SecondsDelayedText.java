package sk.ivankohut.quantifa.utils;

import org.cactoos.Text;
import org.cactoos.text.TextEnvelope;
import org.cactoos.text.TextOf;

public class SecondsDelayedText extends TextEnvelope {

    public SecondsDelayedText(Text text, int seconds) {
        super(new TextOf(new DelayedScalar<>(text::asString, seconds * 1000L)));
    }
}
