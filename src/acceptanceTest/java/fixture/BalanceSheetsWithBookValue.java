package fixture;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.cactoos.list.Joined;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class BalanceSheetsWithBookValue {

    private static final Map<String, List<Map.Entry<LocalDate, Map<String, BigDecimal>>>> values = new HashMap<>();

    private final String type;
    private LocalDate date;
    private BigDecimal totalEquity;
    private BigDecimal commonSharesOutstanding;
    private BigDecimal preferredSharesOutstanding;

    public void execute() {
        addStatement(type, date, Map.of("QTLE", totalEquity, "QTCO", commonSharesOutstanding, "QTPO", preferredSharesOutstanding));
    }

    public void beginTable() {
        values.remove(type);
    }

    public static void addStatement(String type, LocalDate date, Map<String, BigDecimal> values) {
        BalanceSheetsWithBookValue.values.merge(
                type,
                List.of(Map.entry(date, values)),
                (a, b) -> new Joined<Map.Entry<LocalDate, Map<String, BigDecimal>>>(a, b)
        );
    }

    public static Set<Map.Entry<String, List<Map.Entry<LocalDate, Map<String, BigDecimal>>>>> getValues() {
        return values.entrySet();
    }
}
