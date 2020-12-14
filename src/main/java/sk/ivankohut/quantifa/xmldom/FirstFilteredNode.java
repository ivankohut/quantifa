package sk.ivankohut.quantifa.xmldom;

import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.ScalarEnvelope;
import org.w3c.dom.Node;

import java.util.function.Predicate;

public class FirstFilteredNode extends ScalarEnvelope<Node> {

    public FirstFilteredNode(String name, Iterable<Node> nodes) {
        this(n -> name.equals(n.getNodeName()), nodes);
    }

    public FirstFilteredNode(Predicate<Node> predicate, Iterable<Node> nodes) {
        super(
                new FirstOf<>(
                        predicate::test,
                        nodes,
                        () -> {
                            throw new IllegalArgumentException("No node exists satisfying given condition.");
                        }
                )
        );
    }
}
