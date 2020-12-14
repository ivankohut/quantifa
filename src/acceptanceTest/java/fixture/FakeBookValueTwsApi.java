package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.Concatenated;
import org.cactoos.text.Joined;
import sk.ivankohut.quantifa.BalanceSheet;
import sk.ivankohut.quantifa.StockContract;

import java.util.List;
import java.util.Map;

import static fixture.FakePriceMarketDataTwsApi.areStockContractsEqual;

@RequiredArgsConstructor
public class FakeBookValueTwsApi extends TwsApiAdapter {

    private final StockContract stockContract;
    private final Map<String, List<BalanceSheet>> bookValues;

    @SneakyThrows @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        if (areStockContractsEqual(this.stockContract, stockContract)) {
            handler.fundamentals(new Concatenated(
                    () -> "<ReportFinancialStatements><Notes/><FinancialStatements>",
                    new Joined("", new Mapped<>(
                            entry -> {
                                var periods = entry.getKey() + "Periods";
                                return "<" + periods + ">" + new Joined(
                                        "",
                                        new Mapped<>(
                                                bookValue -> """
                                                        <FiscalPeriod>
                                                            <Statement Type="BAL">
                                                                <FPHeader>
                                                                    <StatementDate>%s</StatementDate>
                                                                </FPHeader>
                                                                <lineItem coaCode="STBP">%s</lineItem>
                                                            </Statement>
                                                        </FiscalPeriod>"""
                                                        .formatted(bookValue.date(), bookValue.bookValue()),
                                                entry.getValue())
                                ).asString() + "</" + periods + ">";
                            },
                            bookValues.entrySet()
                    )),
                    () -> "</FinancialStatements></ReportFinancialStatements>"
            ).asString());
        } else {
            throw new IllegalArgumentException("Expected arguments not provided.");
        }
    }
}
