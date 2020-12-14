package sk.ivankohut.quantifa.xmldom;

import lombok.RequiredArgsConstructor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;

@RequiredArgsConstructor
public class ListOfNodes implements Iterable<Node> {

    private final NodeList nodeList;

    @Override
    public Iterator<Node> iterator() {
        return new ListOfNodesIterator(nodeList);
    }
}
