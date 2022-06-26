package fixture;

import java.math.BigDecimal;
import java.util.Map;

public class IncomeStatementXml extends FinancialStatementXml {

    public IncomeStatementXml(Map<String, BigDecimal> values) {
        super("INC", values);
    }

    public IncomeStatementXml() {
        this(Map.of());
    }
}
