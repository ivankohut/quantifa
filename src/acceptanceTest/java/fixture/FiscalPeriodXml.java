package fixture;

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.text.Joined;
import org.cactoos.list.ListOf;

@RequiredArgsConstructor
public class FiscalPeriodXml implements Text {

    private final Iterable<Text> financialStatements;

    public FiscalPeriodXml(Text financialStatement) {
        this(new ListOf<Text>(financialStatement));
    }

    @Override
    public String asString() throws Exception {
        return "<FiscalPeriod>%n%s</FiscalPeriod>%n".formatted(
                new Joined(() -> "", financialStatements).asString()
        );
    }
}
