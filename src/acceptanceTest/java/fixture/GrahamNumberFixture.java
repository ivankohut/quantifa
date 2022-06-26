package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.utils.ContentOfUriTest;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@Setter
public class GrahamNumberFixture {

    private BigDecimal earnings;
    private BigDecimal bookValue;
    private BigDecimal price;

    private Application application;

    public void execute() {
        CacheUtils.clear();
        var fundamentalsStockContract = new SimpleStockContract("exchange", "symbol", "currency");
        var priceStockContract = new SimpleStockContract("priceExchange", "priceSymbol", "priceCurrency");
        var reportingCurrency = "reportingCurrency";
        var fiscalPeriodEndDate = LocalDate.now();
        this.application = new Application(
                new FakeTwsApi(
                        Map.of(priceStockContract, Map.of(TickType.DELAYED_BID, price)),
                        fundamentalsStockContract,
                        new ReportFinancialStatementsXml(
                                reportingCurrency,
                                new PeriodsXml("Annual", new FiscalPeriodXml(
                                        fiscalPeriodEndDate,
                                        new IncomeStatementXml(Map.of("SDBF", earnings)),
                                        new BalanceSheetXml(Map.of("QTLE", bookValue, "QTCO", BigDecimal.ONE))
                                )),
                                new SingleSimplePeriodPeriodsXml("Interim", fiscalPeriodEndDate)
                        )
                ),
                Clock.systemDefaultZone(),
                fundamentalsStockContract,
                CacheUtils.DIRECTORY,
                new SimplePriceRequest(priceStockContract),
                ContentOfUriTest.createHttpClient(
                        "https://financialmodelingprep.com/api/v3/fx?apikey=fmpApiKey",
                        Duration.ofSeconds(15),
                        200,
                        new FmpExchangeRatesJson(reportingCurrency, priceStockContract.currency(), BigDecimal.ONE)
                ),
                "fmpApiKey",
                ""
        );
    }

    public BigDecimal grahamNumber() {
        return application.grahamNumber();
    }

    public BigDecimal grahamRatio() {
        return application.grahamRatio();
    }
}

