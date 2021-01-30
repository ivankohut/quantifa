package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;
import sk.ivankohut.quantifa.utils.PeekedScalar;

@RequiredArgsConstructor
public class TextCache implements Text {

    private final Store store;
    private final Text fileName;
    private final Text content;

    @Override
    // probably bug in SonarQube rule
    @SuppressWarnings("java:S2293")
    public String asString() {
        var fileNameString = new UncheckedText(fileName).asString();
        return new Unchecked<>(new FirstOf<String>(
                new Mapped<>(
                        file -> file.content().asString(),
                        new Filtered<>(file -> file.name().equals(fileNameString), store.files(() -> "."))
                ),
                new PeekedScalar<>(
                        content::asString,
                        c -> store.newFile(
                                new TextOf("."),
                                fileNameString,
                                c
                        )
                )
        )).value();
    }
}
