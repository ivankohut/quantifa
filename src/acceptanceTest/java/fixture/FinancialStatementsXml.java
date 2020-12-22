package fixture;

import org.cactoos.Text;
import org.cactoos.text.Concatenated;
import org.cactoos.text.TextEnvelope;

public class FinancialStatementsXml extends TextEnvelope {

    public FinancialStatementsXml(Text... body) {
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
}
