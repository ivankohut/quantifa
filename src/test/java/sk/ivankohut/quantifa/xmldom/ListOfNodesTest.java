package sk.ivankohut.quantifa.xmldom;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ListOfNodesTest {

    @Test
    void isIterableOfNodesInGivenNodeList() {
        var node1 = mock(Node.class);
        var node2 = mock(Node.class);
        // exercise
        var sut = new ListOfNodes(new ArrayNodeList(node1, node2));
        // verify
        assertThat(sut).containsExactly(node1, node2);
    }
}
