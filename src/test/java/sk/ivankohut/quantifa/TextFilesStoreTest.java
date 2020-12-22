package sk.ivankohut.quantifa;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is non-unit test since it touches the file system.
 */
class TextFilesStoreTest {

    private final Store sut = new TextFilesStore("testStore");

    @AfterAll
    private static void cleanup() throws IOException {
        FileUtils.deleteDirectory(new File("testStore"));
    }

    @Test
    void emptyDirectory() {
        // exercise
        var result = sut.files(() -> "non-existent");
        // verify
        assertThat(result).isEmpty();
    }

    @Test
    void storesAndLoadsTextFile() throws Exception {
        // exercise
        sut.newFile(() -> "directory", "name", "content");
        var result = sut.files(() -> "directory");
        // verify
        assertThat(result).hasSize(1);
        var textFile = result.iterator().next();
        assertThat(textFile.name()).isEqualTo("name");
        assertThat(textFile.content().asString()).isEqualTo("content");
    }
}
