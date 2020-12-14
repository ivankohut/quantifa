package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;
import org.w3c.dom.Node;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class XmlBalanceSheet implements BalanceSheet {

    private final Scalar<Node> date;
    private final Scalar<Node> bookValue;

    @Override
    public LocalDate date() {
        return LocalDate.parse(textContent(date));
    }

    @Override
    public BigDecimal bookValue() {
        return new BigDecimal(textContent(bookValue));
    }

    private static String textContent(Scalar<Node> node) {
        return new Unchecked<>(node).value().getTextContent();
    }
}
