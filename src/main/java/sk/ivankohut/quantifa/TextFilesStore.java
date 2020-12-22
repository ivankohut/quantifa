package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.io.Directory;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TextFilesStore implements Store {

    private final Path directory;

    public TextFilesStore(String directory) {
        this(Path.of(directory));
    }

    @Override
    public Iterable<TextFile> files(Text directory) {
        var sourceDirectory = this.directory.resolve(new UncheckedText(directory).asString());
        if (Files.exists(sourceDirectory) && Files.isDirectory(sourceDirectory)) {
            return new Mapped<>(
                    file -> new TextFile() {

                        @Override
                        public String name() {
                            return Optional.ofNullable(file.getFileName()).orElse(Path.of("")).toString();
                        }

                        @Override
                        public Text content() {
                            return new TextOf(file);
                        }
                    },
                    new Filtered<>(Files::isRegularFile, new Directory(sourceDirectory))
            );
        } else {
            return List.of();
        }
    }

    @Override
    public void newFile(Text directory, String name, String content) {
        try {
            var destinationDirectory = this.directory.resolve(new UncheckedText(directory).asString());
            Files.createDirectories(destinationDirectory);
            Files.writeString(destinationDirectory.resolve(name), content);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
