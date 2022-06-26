package fixture;

import org.cactoos.text.TextEnvelope;

import java.time.LocalDate;

public class SingleSimplePeriodPeriodsXml extends TextEnvelope {

    public SingleSimplePeriodPeriodsXml(String type, LocalDate fiscalPeriodEndDate) {
        // both statements are necessary because fiscal period
        // must have at least one income statement and at least one balance sheet
        super(new PeriodsXml(type, new FiscalPeriodXml(
                fiscalPeriodEndDate,
                new IncomeStatementXml(),
                new BalanceSheetXml()
        )));
    }
}
