package sk.ivankohut.quantifa.xmldom;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChildNodesTest {

    @Test
    void providesHildNodesOfGivenNode() {
        var child = mock(Node.class);
        // exercise
        var sut = new ChildNodes(when(mock(Node.class).getChildNodes()).thenReturn(new ArrayNodeList(child)).<Node>getMock());
        // verify
        assertThat(sut).containsExactly(child);
    }
}
