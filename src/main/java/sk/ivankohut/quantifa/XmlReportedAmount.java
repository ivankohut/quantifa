package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.cactoos.scalar.Unchecked;
import org.w3c.dom.Node;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class XmlReportedAmount implements ReportedAmount {

    private final Scalar<Node> date;
    private final Scalar<Node> value;

    @Override
    public LocalDate date() {
        return LocalDate.parse(textContent(date));
    }

    @Override
    public BigDecimal value() {
        return new BigDecimal(textContent(value));
    }

    private static String textContent(Scalar<Node> node) {
        return new Unchecked<>(node).value().getTextContent();
    }
}
