package fixture;

import org.apache.commons.io.FileUtils;
import org.cactoos.Text;
import sk.ivankohut.quantifa.TextFilesStore;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.LocalDate;

public final class CacheUtils {

    public static final Path DIRECTORY = Path.of("build/tmp/acceptanceTestCache");

    private CacheUtils() {
        // empty
    }

    public static void resetWith(Text directory, Iterable<LocalDate> fileNames, String extension, String content) {
        clear();
        var store = new TextFilesStore(DIRECTORY);
        fileNames.forEach(date -> store.newFile(directory, date + extension, content));
    }

    public static void clear() {
        try {
            FileUtils.deleteDirectory(DIRECTORY.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
