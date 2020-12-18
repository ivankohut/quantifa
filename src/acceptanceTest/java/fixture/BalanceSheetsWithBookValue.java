package fixture;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.cactoos.list.Joined;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.SimpleReportedAmount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class BalanceSheetsWithBookValue {

    private static final Map<String, List<ReportedAmount>> values = new HashMap<>();

    private final String type;
    private BigDecimal bookValue;
    private LocalDate date;

    public void execute() {
        values.merge(type, List.of(new SimpleReportedAmount(date, bookValue)), (a, b) -> new Joined<ReportedAmount>(a, b));
    }

    public void beginTable() {
        values.remove(type);
    }

    public static Map<String, List<ReportedAmount>> getValues() {
        return values;
    }
}
