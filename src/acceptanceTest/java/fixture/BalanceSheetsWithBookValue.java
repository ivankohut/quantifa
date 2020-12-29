package fixture;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.cactoos.list.Joined;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        values.merge(
                type,
                List.of(Map.entry(date, Map.of("QTLE", totalEquity, "QTCO", commonSharesOutstanding, "QTPO", preferredSharesOutstanding))),
                (a, b) -> new Joined<Map.Entry<LocalDate, Map<String, BigDecimal>>>(a, b)
        );
    }

    public void beginTable() {
        values.remove(type);
    }

    public static Map<String, List<Map.Entry<LocalDate, Map<String, BigDecimal>>>> getValues() {
        return values;
    }
}
