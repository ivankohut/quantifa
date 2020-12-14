package fixture;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.cactoos.list.Joined;
import sk.ivankohut.quantifa.BalanceSheet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class BalanceSheetsWithBookValue {

    private static final Map<String, List<BalanceSheet>> values = new HashMap<>();

    private final String type;
    private BigDecimal bookValue;
    private LocalDate date;

    public void execute() {
        values.merge(type, List.of(new SimpleBalanceSheet(date, bookValue)), (a, b) -> new Joined<BalanceSheet>(a, b));
    }

    public void beginTable() {
        values.remove(type);
    }

    public static Map<String, List<BalanceSheet>> getValues() {
        return values;
    }
}
