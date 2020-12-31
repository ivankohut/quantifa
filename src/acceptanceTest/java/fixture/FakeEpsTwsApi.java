package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import lombok.SneakyThrows;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.Joined;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.StockContract;

import java.util.List;

import static fixture.FakePriceMarketDataTwsApi.areStockContractsEqual;

public class FakeEpsTwsApi extends TwsApiAdapter {

    private final StockContract stockContract;
    private final List<ReportedAmount> dateAndAmount;
    private final String period;

    public FakeEpsTwsApi(StockContract stockContract, List<ReportedAmount> dateAndAmount, String period) {
        this.stockContract = stockContract;
        this.dateAndAmount = dateAndAmount;
        this.period = period;
    }

    @SneakyThrows
    @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        if (areStockContractsEqual(this.stockContract, stockContract)) {
            var periods = period + "Periods";
            handler.fundamentals(new FinancialStatementsXml(
                    () -> "<%s>".formatted(periods),
                    new Joined(
                            "",
                            new Mapped<>(
                                    eps -> """
                                            <FiscalPeriod>
                                                <Statement Type="INC">
                                                    <FPHeader>
                                                        <StatementDate>%s</StatementDate>
                                                    </FPHeader>
                                                    <lineItem coaCode="VDES">%s</lineItem>
                                                </Statement>
                                            </FiscalPeriod>"""
                                            .formatted(eps.date(), eps.value()),
                                    dateAndAmount
                            )
                    ),
                    () -> "</%s>".formatted(periods)
            ).asString());
        } else {
            throw new IllegalArgumentException("Expected arguments not provided.");
        }
    }
}
