package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.Joined;
import sk.ivankohut.quantifa.StockContract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static fixture.FakePriceMarketDataTwsApi.areStockContractsEqual;

@RequiredArgsConstructor
public class FakeBookValueTwsApi extends TwsApiAdapter {

    private final StockContract stockContract;
    private final Map<String, List<Map.Entry<LocalDate, Map<String, BigDecimal>>>> bookValues;

    @SneakyThrows @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        if (areStockContractsEqual(this.stockContract, stockContract)) {
            handler.fundamentals(new FinancialStatementsXml(
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
                                                                %s
                                                            </Statement>
                                                        </FiscalPeriod>"""
                                                        .formatted(bookValue.getKey(), new Mapped<>(
                                                                e -> "<lineItem coaCode=\"%s\">%s</lineItem>".formatted(e.getKey(), e.getValue()),
                                                                bookValue.getValue().entrySet()
                                                        )),
                                                entry.getValue())
                                ).asString() + "</" + periods + ">";
                            },
                            bookValues.entrySet()
                    ))
            ).asString());
        } else {
            throw new IllegalArgumentException("Expected arguments not provided.");
        }
    }
}
