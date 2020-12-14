package sk.ivankohut.quantifa.xmldom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListOfNodesIterator implements Iterator<Node> {

    private final NodeList nodeList;
    private final int length;
    private int index;

    public ListOfNodesIterator(NodeList nodeList) {
        this.nodeList = nodeList;
        this.length = nodeList.getLength();
    }

    @Override
    public boolean hasNext() {
        return index < length;
    }

    @Override
    public Node next() {
        if (index < length) {
            return nodeList.item(index++);
        } else {
            throw new NoSuchElementException();
        }
    }
}
