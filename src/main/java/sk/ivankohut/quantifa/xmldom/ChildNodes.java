package sk.ivankohut.quantifa.xmldom;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;
import org.w3c.dom.Node;

import java.util.Iterator;

@RequiredArgsConstructor
public class ChildNodes implements Iterable<Node> {

    private final Scalar<Node> node;

    // disabling buggy SonarQube rule reporting false positive here
    @SuppressWarnings("javabugs:S2190")
    public ChildNodes(Node node) {
        this(() -> node);
    }

    @Override
    public Iterator<Node> iterator() {
        return new ListOfNodesIterator(new Unchecked<>(node).value().getChildNodes());
    }
}
