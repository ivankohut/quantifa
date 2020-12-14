package sk.ivankohut.quantifa.xmldom;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.ReaderInputStream;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.UncheckedText;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sk.ivankohut.quantifa.ApplicationException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class XmlDocument implements Scalar<Document> {

    private static final Unchecked<DocumentBuilder> DOCUMENT_BUILDER = new Unchecked<>(new Sticky<>(() -> {
        var factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }));

    private final Text xmlText;
    private final DocumentBuilder documentBuilder;

    public XmlDocument(Text xmlText) {
        this(xmlText, DOCUMENT_BUILDER.value());
    }

    @Override
    public Document value() {
        try {
            return documentBuilder.parse(new ReaderInputStream(new StringReader(new UncheckedText(xmlText).asString()), StandardCharsets.UTF_8));
        } catch (IOException | SAXException e) {
            throw new ApplicationException(e);
        }
    }
}
