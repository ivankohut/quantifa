package sk.ivankohut.quantifa;

import org.cactoos.Scalar;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class XmlReportedAmountTest {

    @Test
    void extractValuesFromGivenXml() {
        // exercise
        var sut = new XmlReportedAmount(createNode("2019-12-31"), createNode("12.410860"));
        // verify
        assertThat(sut.date()).isEqualTo("2019-12-31");
        assertThat(sut.value()).isEqualByComparingTo("12.41086");
    }

    private static Scalar<Node> createNode(String textContent) {
        return () -> when(mock(Node.class).getTextContent()).thenReturn(textContent).getMock();
    }
}
