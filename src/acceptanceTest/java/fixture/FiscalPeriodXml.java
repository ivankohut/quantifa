package fixture;

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.Joined;

import java.time.LocalDate;

@RequiredArgsConstructor
public class FiscalPeriodXml implements Text {

    private final LocalDate endDate;
    private final Iterable<Text> financialStatements;

    public FiscalPeriodXml(LocalDate endDate, Text... financialStatements) {
        this(endDate, new ListOf<>(financialStatements));
    }

    @Override
    public String asString() throws Exception {
        return "<FiscalPeriod EndDate=\"%s\">%n%s</FiscalPeriod>%n".formatted(
                endDate.toString(),
                new Joined(() -> "", financialStatements).asString()
        );
    }
}
