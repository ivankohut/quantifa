package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import lombok.SneakyThrows;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.Concatenated;
import org.cactoos.text.Joined;
import sk.ivankohut.quantifa.ReportedAmount;
import sk.ivankohut.quantifa.StockContract;

import java.util.List;

import static fixture.FakePriceMarketDataTwsApi.areStockContractsEqual;

public class FakeEpsTwsApi extends TwsApiAdapter {

    private final StockContract stockContract;
    private final List<ReportedAmount> dateAndAmount;

    public FakeEpsTwsApi(StockContract stockContract, List<ReportedAmount> dateAndAmount) {
        this.stockContract = stockContract;
        this.dateAndAmount = dateAndAmount;
    }

    @SneakyThrows
    @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        if (areStockContractsEqual(this.stockContract, stockContract)) {
            handler.fundamentals(new Concatenated(
                    () -> "<ReportFinancialStatements><Notes/><FinancialStatements><InterimPeriods>",
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
                    () -> "</InterimPeriods></FinancialStatements></ReportFinancialStatements>"
            ).asString());
        } else {
            throw new IllegalArgumentException("Expected arguments not provided.");
        }
    }
}
