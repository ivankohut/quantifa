package sk.ivankohut.quantifa.xmldom;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FirstFilteredNodeTest {

    @Test
    void firstNodeWithGivenName() throws Exception {
        var name = "name";
        var node = createNode(name);
        // exercise
        var sut = new FirstFilteredNode(name, Arrays.asList(createNode(name + "X"), node, createNode(name)));
        // verify
        assertThat(sut.value()).isEqualTo(node);
    }

    @Test
    void exceptionIfNoNodeWithGivenNameExists() {
        var name = "name";
        // exercise
        var sut = new FirstFilteredNode("name", Collections.singletonList(createNode(name + "X")));
        // verify
        assertThatThrownBy(sut::value)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No node exists satisfying given condition.");
    }

    private static Node createNode(String name) {
        return when(mock(Node.class).getNodeName()).thenReturn(name).getMock();
    }
}
