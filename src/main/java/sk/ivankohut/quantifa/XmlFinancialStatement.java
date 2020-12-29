package sk.ivankohut.quantifa;

import lombok.RequiredArgsConstructor;
import org.cactoos.Scalar;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.FirstOf;
import org.cactoos.scalar.Unchecked;
import org.w3c.dom.Node;
import sk.ivankohut.quantifa.xmldom.ChildNodes;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class XmlFinancialStatement implements FinancialStatement {

    private final Scalar<LocalDate> date;
    private final Scalar<? extends Node> statement;

    @Override
    public LocalDate date() {
        return new Unchecked<>(date).value();
    }

    @Override
    public BigDecimal value(String name) {
        var lineItems = new Filtered<>(n -> "lineItem".equals(n.getNodeName()), new ChildNodes(new Unchecked<>(statement).value()));
        return new Unchecked<>(new FirstOf<>(
                new Mapped<>(
                        node -> new BigDecimal(node.getTextContent()),
                        new Filtered<Node>(n -> name.equals(n.getAttributes().getNamedItem("coaCode").getTextContent()), lineItems)
                ),
                BigDecimal.ZERO
        )).value();
    }
}
