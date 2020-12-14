package sk.ivankohut.quantifa.xmldom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;

public class ArrayNodeList implements NodeList {

    private final Node[] nodes;

    public ArrayNodeList(Node... nodes) {
        this.nodes = Arrays.copyOf(nodes, nodes.length);
    }

    @Override
    public Node item(int index) {
        return nodes[index];
    }

    @Override
    public int getLength() {
        return nodes.length;
    }
}
