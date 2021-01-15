package fixture;

import lombok.RequiredArgsConstructor;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.SimpleReportedAmount;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DilutedNormalizedEpsTtmFixture {

    private List<ReportedAmount> dateAndAmount;

    public BigDecimal eps() {
        StoredStatementsFixture.clearCache();
        var stockContract = new SimpleStockContract("exchange", "symbol", "currency");
        return new FixtureApplication(new FakeEpsTwsApi(stockContract, dateAndAmount, "Interim"), stockContract).epsTtm();
    }

    public void setDateAndAmount(Map<String, String> dateAndAmount) {
        this.dateAndAmount =
                new ListOf<>(new Mapped<>(e -> new SimpleReportedAmount(LocalDate.parse(e.getKey()), new BigDecimal(e.getValue())), dateAndAmount.entrySet()));
    }
}
