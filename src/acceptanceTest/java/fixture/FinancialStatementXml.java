package fixture;

import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.Joined;

import java.math.BigDecimal;
import java.util.Map;

public class FinancialStatementXml implements Text {

    private final String type;
    private final Map<String, BigDecimal> values;

    public FinancialStatementXml(String type, Map<String, BigDecimal> values) {
        this.type = type;
        this.values = values;
    }

    @Override
    public String asString() {
        return """
                <Statement Type="%s">
                    %s</Statement>
                """.formatted(type, new Joined(
                "    ", new Mapped<>(e -> "<lineItem coaCode=\"%s\">%s</lineItem>%n".formatted(e.getKey(), e.getValue()), values.entrySet())
        ));
    }
}
