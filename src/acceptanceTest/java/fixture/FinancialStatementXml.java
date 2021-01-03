package fixture;

import org.cactoos.Text;
import org.cactoos.iterable.Mapped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class FinancialStatementXml implements Text {

    private final String type;
    private final LocalDate date;
    private final Map<String, BigDecimal> values;

    public FinancialStatementXml(String type, LocalDate date, Map<String, BigDecimal> values) {
        this.type = type;
        this.date = date;
        this.values = values;
    }

    @Override
    public String asString() {
        return """
                <Statement Type="%s">
                    <FPHeader>
                        <StatementDate>%s</StatementDate>
                    </FPHeader>
                    %s</Statement>
                """.formatted(type, date, new Mapped<>(e -> "<lineItem coaCode=\"%s\">%s</lineItem>%n".formatted(e.getKey(), e.getValue()), values.entrySet()));
    }
}
