package sk.ivankohut.quantifa;

import org.cactoos.Text;

public interface Store {

    Iterable<TextFile> files(Text directory);

    void newFile(Text directory, String name, String content);
}
