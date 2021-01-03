package fixture;

import lombok.RequiredArgsConstructor;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.Joined;

@RequiredArgsConstructor
public class PeriodsXml implements Text {

    private final String type;
    private final Iterable<? extends Text> fiscalPeriods;

    public PeriodsXml(String type, Text... fiscalPeriods) {
        this(type, new ListOf<>(fiscalPeriods));
    }

    @Override
    public String asString() throws Exception {
        var periods = type + "Periods";
        return "<" + periods + ">" + new Joined(() -> "", fiscalPeriods).asString() + "</" + periods + ">";
    }
}
