package fixture;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceSheetXml extends FinancialStatementXml {

    public BalanceSheetXml(Map<String, BigDecimal> values) {
        super("BAL", values);
    }

    public BalanceSheetXml() {
        this(Map.of());
    }
}
