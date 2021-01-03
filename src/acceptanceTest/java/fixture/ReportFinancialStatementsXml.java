package fixture;

import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.Concatenated;
import org.cactoos.text.TextEnvelope;

public class ReportFinancialStatementsXml extends TextEnvelope {

    public ReportFinancialStatementsXml(Iterable<? extends Text> body) {
        super(new Concatenated(
                () -> """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <ReportFinancialStatements>
                          <CoGeneralInfo>
                            <LatestAvailableInterim>2020-09-30</LatestAvailableInterim>
                          </CoGeneralInfo>
                          <FinancialStatements>
                        """,
                new Concatenated(body),
                () -> "</FinancialStatements></ReportFinancialStatements>"
        ));
    }

    public ReportFinancialStatementsXml(Text... body) {
        this(new ListOf<>(body));
    }
}
