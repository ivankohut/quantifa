package sk.ivankohut.quantifa.xmldom;

import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.ApplicationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XmlDocumentTest {

    @Test
    void parsesGivenXmlToDom() {
        var sut = create("<a>b</a>");
        // exercise
        var result = sut.value();
        // verify
        assertThat(result.getDocumentElement().getNodeName()).isEqualTo("a");
        assertThat(result.getDocumentElement().getTextContent()).isEqualTo("b");
    }

    @Test
    void exceptionWhenXmlIsInvalid() {
        var sut = create("<a>b</c>");
        // exercise
        assertThatThrownBy(sut::value).isInstanceOf(ApplicationException.class);
    }

    public static XmlDocument create(String xml) {
        return new XmlDocument(new TextOf(xml));
    }
}
