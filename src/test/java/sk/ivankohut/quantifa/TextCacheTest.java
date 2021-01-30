package sk.ivankohut.quantifa;

import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.UncheckedText;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TextCacheTest {

    @Test
    void providesCachedText() {
        var cachedContent = "cachedContent";
        var fileName = "fileName";
        var store = createStore(cachedContent, fileName, ".");
        var sut = new TextCache(store, () -> fileName, () -> cachedContent + "X");
        // exercise
        var result = sut.asString();
        // verify
        assertThat(result).isEqualTo(cachedContent);
    }

    @Test
    void providesAndStoresNotExistingFile() {
        var newContent = "newContent";
        var fileName = "fileName";
        var store = createStore("cachedContent", fileName + "X", ".");
        var sut = new TextCache(store, () -> fileName, () -> newContent);
        // exercise
        var result = sut.asString();
        // verify
        verify(store).newFile(ArgumentMatchers.argThat(text -> ".".equals(new UncheckedText(text).asString())), eq(fileName), eq(newContent));
        assertThat(result).isEqualTo(newContent);
    }

    private static Store createStore(String cachedContent, String fileName, String directory) {
        var store = mock(Store.class);
        when(store.files(any(Text.class))).thenAnswer(invocation -> {
            assertThat(invocation.getArgument(0, Text.class).asString()).isEqualTo(directory);
            return new ListOf<>(
                    new SimpleTextFile(fileName + "X", cachedContent + "Y"),
                    new SimpleTextFile(fileName, cachedContent)
            );
        });
        return store;
    }
}
