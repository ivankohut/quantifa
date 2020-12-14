package sk.ivankohut.quantifa.xmldom;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ListOfNodesIteratorTest {

    @Test
    void iteratesOverGivenNodeList() {
        // exercise
        var node1 = mock(Node.class);
        var node2 = mock(Node.class);
        var sut = new ListOfNodesIterator(new ArrayNodeList(node1, node2));
        // verify
        assertThat(sut.hasNext()).isTrue();
        assertThat(sut.next()).isEqualTo(node1);
        assertThat(sut.hasNext()).isTrue();
        assertThat(sut.next()).isEqualTo(node2);
        assertThat(sut.hasNext()).isFalse();
        assertThatThrownBy(sut::next).isInstanceOf(NoSuchElementException.class);
    }
}
