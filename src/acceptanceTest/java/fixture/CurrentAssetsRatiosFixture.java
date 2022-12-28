package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import org.cactoos.list.Joined;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.utils.ContentOfUriTest;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("PMD.ImmutableField")
public class CurrentAssetsRatiosFixture {

    private Optional<BigDecimal> currentAssets;
    private Optional<BigDecimal> currentLiabilities;
    private Optional<BigDecimal> longTermDebt;
    @Setter
    private BigDecimal commonShares = BigDecimal.ZERO;
    @Setter
    private BigDecimal preferredShares = BigDecimal.ZERO;
    @Setter
    private BigDecimal price = BigDecimal.ZERO;

    private Application application;

    public void setCurrentAssets(BigDecimal currentAssets) {
        this.currentAssets = Optional.ofNullable(currentAssets);
    }

    public void setCurrentLiabilities(BigDecimal currentLiabilities) {
        this.currentLiabilities = Optional.ofNullable(currentLiabilities);
    }

    public void setLongTermDebt(BigDecimal longTermDebt) {
        this.longTermDebt = Optional.ofNullable(longTermDebt);
    }

    @SuppressWarnings("unchecked")
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
                                new PeriodsXml("Interim", new FiscalPeriodXml(
                                        fiscalPeriodEndDate,
                                        new BalanceSheetXml(Map.ofEntries(new Joined<>(
                                                entryList("ATCA", currentAssets),
                                                entryList("LTCL", currentLiabilities),
                                                entryList("LTTD", longTermDebt),
                                                entryList("QTCO", Optional.of(commonShares)),
                                                entryList("QTPO", Optional.of(preferredShares))
                                        ).toArray(new Map.Entry[] {})))
                                )),
                                new SingleSimplePeriodPeriodsXml("Annual", fiscalPeriodEndDate.minusYears(1))
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

    private static List<Map.Entry<String, BigDecimal>> entryList(String key, Optional<BigDecimal> maybeValue) {
        return maybeValue.map(value -> List.of(Map.entry(key, value))).orElse(List.of());
    }

    public BigDecimal currentRatio() {
        return application.currentRatio();
    }

    public BigDecimal netCurrentAssetsToLongTermDebt() {
        return application.netCurrentAssetsToLongTermDebtRatio();
    }

    public BigDecimal netCurrentAssetValue() {
        return application.netCurrentAssetValue();
    }

    public BigDecimal netCurrentAssetValueRatio() {
        return application.netCurrentAssetValueRatio();
    }
}
