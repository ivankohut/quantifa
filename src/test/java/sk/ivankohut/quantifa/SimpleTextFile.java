package sk.ivankohut.quantifa;

import org.cactoos.Text;

public class SimpleTextFile implements TextFile {

    private final String name;
    private final Text content;

    public SimpleTextFile(String name, Text content) {
        this.name = name;
        this.content = content;
    }

    public SimpleTextFile(String name, String content) {
        this(name, () -> content);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Text content() {
        return content;
    }
}
