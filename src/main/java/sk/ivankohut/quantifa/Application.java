package sk.ivankohut.quantifa;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.Equals;
import org.cactoos.scalar.Or;
import org.cactoos.scalar.ScalarOfSupplier;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.Concatenated;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.cactoos.text.TextOfDateTime;
import org.cactoos.text.UncheckedText;
import org.json.JSONObject;
import sk.ivankohut.quantifa.decimal.DecimalOf;
import sk.ivankohut.quantifa.decimal.DivisionOf;
import sk.ivankohut.quantifa.decimal.MultiplicationOf;
import sk.ivankohut.quantifa.decimal.Negated;
import sk.ivankohut.quantifa.decimal.NonNegative;
import sk.ivankohut.quantifa.decimal.Rounded;
import sk.ivankohut.quantifa.decimal.SquareRootOf;
import sk.ivankohut.quantifa.decimal.SumOf;
import sk.ivankohut.quantifa.utils.ContentOfUri;
import sk.ivankohut.quantifa.utils.SecondsDelayedText;
import sk.ivankohut.quantifa.utils.PeekedScalar;
import sk.ivankohut.quantifa.utils.StickyFirstOrFail;
import sk.ivankohut.quantifa.xmldom.XPathNodes;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings({ "PMD.DataClass", "PMD.ExcessiveImports", "PMD.TooManyMethods" })
public class Application {

    private final Text companyName;
    private final MarketPrice price;
    private final ReportedAmount bookValue;
    private final Scalar<BigDecimal> epsTtm;
    private final Scalar<BigDecimal> epsAverage;
    private final Scalar<BigDecimal> grahamNumber;
    private final Scalar<BigDecimal> grahamRatio;
    private final Scalar<BigDecimal> currentRatio;
    private final Scalar<BigDecimal> netCurrentAssetsToLongTermDebtRatio;
    private final Scalar<BigDecimal> netCurrentAssetValue;
    private final Scalar<BigDecimal> netCurrentAssetValueRatio;

    // checkstyle nor pmd does not properly support switch expression yet
    @SuppressWarnings({ "checkstyle:Indentation", "checkstyle:WhitespaceAround", "PMD.UselessParentheses", "PMD.ExcessiveMethodLength", "java:S107" })
    public Application(
            TwsApi twsApi,
            Clock clock,
            StockContract fundamentalsRequest,
            Path cacheDirectory,
            PriceRequest priceRequest,
            HttpClient httpClient,
            String fmpApiKey,
            String avApiKey
    ) {
        var today = LocalDate.now(clock);
        var financialStatements = new CachedFinancialStatements(
                new TextFilesStore(cacheDirectory.resolve("financialStatements")),
                today,
                new Joined("-", fundamentalsRequest.exchange(), fundamentalsRequest.symbol(), fundamentalsRequest.currency()),
                new TwsFundamental(twsApi, fundamentalsRequest, Types.FundamentalType.ReportsFinStatements),
                13,
                ".xml"
        );
        this.companyName = () -> new XPathNodes(financialStatements, "/ReportFinancialStatements/CoIDs/CoID[@Type='CompanyName']")
                .iterator().next().getTextContent();
        var timeout = Duration.ofSeconds(15);
        var priceFileName = new Concatenated(
                new TextOfDateTime(DateTimeFormatter.ISO_LOCAL_DATE, today),
                new TextOf(".json")
        );
        this.price = () -> (switch (priceRequest.source()) {
            case "TWS" -> new TwsMarketPriceOfStock(twsApi, priceRequest, true);
            case "FMP" -> new FmpMarketPriceOfStock(
                    new TextCache(
                            new TextFilesStore(cacheDirectory.resolve("prices/fmp")),
                            priceFileName,
                            new TextOf(
                                    new PeekedScalar<>(
                                            () -> new ContentOfUri(
                                                    httpClient,
                                                    new Concatenated("https://financialmodelingprep.com/api/v3/stock/list?apikey=", fmpApiKey),
                                                    timeout
                                            ).asString(),
                                            s -> {
                                                var key = "Error Message";
                                                if (s.contains(key)) {
                                                    throw new ApplicationException(new JSONObject(s).getString(key));
                                                }
                                            }
                                    )
                            )
                    ),
                    priceRequest.symbol()
            );
            case "AV" -> new AvMarketPriceOfStock(
                    new TextCache(
                            new TextFilesStore(cacheDirectory.resolve("prices/av/" + priceRequest.symbol())),
                            priceFileName,
                            new AvValidatedResponse(
                                    new SecondsDelayedText(
                                            new ContentOfUri(
                                                    httpClient,
                                                    new Concatenated(
                                                            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=",
                                                            priceRequest.symbol(),
                                                            "&apikey=",
                                                            avApiKey
                                                    ),
                                                    timeout
                                            ),
                                            priceRequest.sourceCoolDownSeconds()
                                    )
                            )
                    )
            );
            case "YF" -> new YfMarketPriceOfStock(
                    new TextCache(
                            new TextFilesStore(cacheDirectory.resolve("prices/yf/" + priceRequest.symbol())),
                            priceFileName,
                            new SecondsDelayedText(
                                    new ContentOfUri(
                                            httpClient,
                                            new Concatenated(
                                                    "https://finance.yahoo.com/quote/",
                                                    priceRequest.symbol()
                                            ),
                                            timeout
                                    ),
                                    priceRequest.sourceCoolDownSeconds()
                            )
                    )
            );
            default -> throw new IllegalArgumentException("Unknown price source.");
        }).price().map(p -> p.divide(BigDecimal.valueOf(priceRequest.divisor())))
                .flatMap(p -> new FmpCurrencyExchangeRate(
                        new TextCache(
                                new TextFilesStore(cacheDirectory.resolve("forex/fmp")),
                                priceFileName,
                                new TextOf(
                                        new PeekedScalar<>(
                                                () -> new ContentOfUri(
                                                        httpClient,
                                                        new Concatenated("https://financialmodelingprep.com/api/v3/fx?apikey=", fmpApiKey),
                                                        timeout
                                                ).asString(),
                                                s -> {
                                                    var key = "Error Message";
                                                    if (s.contains(key)) {
                                                        throw new ApplicationException(new JSONObject(s).getString(key));
                                                    }
                                                }
                                        )
                                )
                        ),
                        priceRequest.currency(),
                        new XPathNodes(financialStatements, "/ReportFinancialStatements/CoGeneralInfo/ReportingCurrency")
                                .iterator().next().getAttributes().getNamedItem("Code").getTextContent()
                ).price().map(p::multiply).map(BigDecimal::stripTrailingZeros));

        var financialStatementsNode = new StickyFirstOrFail<>(
                new XPathNodes(financialStatements, "/ReportFinancialStatements/FinancialStatements"),
                "No financial statements available."
        );

        var annualFiscalPeriods = new FiscalPeriods(financialStatementsNode, "Annual");
        var interimFiscalPeriods = new FiscalPeriods(financialStatementsNode, "Interim");

        var annualBalanceSheet = new MostRecentFinancialStatement(new Mapped<>(FiscalPeriod::balanceSheet, annualFiscalPeriods));
        var interimBalanceSheet = new MostRecentFinancialStatement(new Mapped<>(FiscalPeriod::balanceSheet, interimFiscalPeriods));
        var mostRecentBalanceSheet = new FinancialStatement() {

            private final Unchecked<FinancialStatement> scalar = new Unchecked<>(new Sticky<>(
                    new Ternary<>(
                            new ScalarOfSupplier<>(() -> interimBalanceSheet.date().isAfter(annualBalanceSheet.date())),
                            interimBalanceSheet,
                            annualBalanceSheet
                    )
            ));

            @Override
            public LocalDate date() {
                return scalar.value().date();
            }

            @Override
            public Scalar<BigDecimal> value(String name) {
                return () -> scalar.value().value(name).value();
            }
        };
        var sharesCount = new SumOf(mostRecentBalanceSheet.value("QTCO"), mostRecentBalanceSheet.value("QTPO"));
        this.bookValue = new ReportedAmount() {

            @Override
            public LocalDate date() {
                return mostRecentBalanceSheet.date();
            }

            @Override
            public Scalar<BigDecimal> value() {
                return new DivisionOf(
                        mostRecentBalanceSheet.value("QTLE"),
                        sharesCount,
                        new BigDecimal(-1)
                );
            }
        };
        this.epsTtm = new TrailingTwelveMonths(new EpsList(interimFiscalPeriods));
        this.epsAverage = new AverageOfTheMostRecent(new EpsList(annualFiscalPeriods), 3);
        var totalCurrentAssets = mostRecentBalanceSheet.value("ATCA");
        var totalCurrentLiabilities = mostRecentBalanceSheet.value("LTCL");
        this.currentRatio = new DivisionOf(totalCurrentAssets, totalCurrentLiabilities, BigDecimal.ZERO);
        Scalar<BigDecimal> zero = () -> BigDecimal.ZERO;
        var noCurrentValue = new Or(
                new Equals<>(totalCurrentAssets, zero),
                new Equals<>(totalCurrentLiabilities, zero)
        );
        var currentValue = new SumOf(totalCurrentAssets, new Negated(totalCurrentLiabilities));
        var longTermDebt = mostRecentBalanceSheet.value("LTTD");
        this.netCurrentAssetsToLongTermDebtRatio = new Ternary<>(
                noCurrentValue,
                zero,
                new DivisionOf(currentValue, longTermDebt, BigDecimal.ZERO)
        );
        this.netCurrentAssetValue = new Ternary<>(
                new Or(noCurrentValue, new Equals<>(longTermDebt, zero)),
                zero,
                new DivisionOf(new SumOf(currentValue, new Negated(longTermDebt)), sharesCount, BigDecimal.ZERO)
        );
        Scalar<BigDecimal> priceOrZero = () -> price.price().orElse(BigDecimal.ZERO);
        this.netCurrentAssetValueRatio = new DivisionOf(priceOrZero, new NonNegative(netCurrentAssetValue), BigDecimal.ZERO);
        this.grahamNumber = new Sticky<>(new SquareRootOf(new MultiplicationOf(
                new DecimalOf(15 * 1.5), new NonNegative(epsAverage), new NonNegative(bookValue.value()))
        ));
        this.grahamRatio = new DivisionOf(priceOrZero, grahamNumber, BigDecimal.ZERO);
    }

    public String companyName() {
        return new UncheckedText(companyName).asString();
    }

    public BigDecimal price() {
        return price.price().orElse(BigDecimal.ZERO);
    }

    public ReportedAmount bookValue() {
        return bookValue;
    }

    public BigDecimal epsTtm() {
        return new Unchecked<>(epsTtm).value();
    }

    public BigDecimal epsAverage() {
        return new Unchecked<>(epsAverage).value();
    }

    public BigDecimal grahamNumber() {
        return new Unchecked<>(new Rounded(grahamNumber)).value();
    }

    public BigDecimal grahamRatio() {
        return new Unchecked<>(new Rounded(grahamRatio)).value();
    }

    public BigDecimal currentRatio() {
        return new Unchecked<>(new Rounded(currentRatio)).value();
    }

    public BigDecimal netCurrentAssetsToLongTermDebtRatio() {
        return new Unchecked<>(new Rounded(netCurrentAssetsToLongTermDebtRatio)).value();
    }

    public BigDecimal netCurrentAssetValue() {
        return new Unchecked<>(new Rounded(netCurrentAssetValue)).value();
    }

    public BigDecimal netCurrentAssetValueRatio() {
        return new Unchecked<>(new Rounded(netCurrentAssetValueRatio)).value();
    }

    @SuppressWarnings({ "PMD.SystemPrintln", "java:S106", "PMD.AvoidPrintStackTrace", "java:S4507", "PMD.AvoidCatchingGenericException" })
    public static void main(String[] args) {
        int status;
        var configuration = new ApplicationConfiguration("environment variable", System.getenv());
        try (var twsApi = new TwsApiController(configuration, new ApiController(new TwsConnectionHandler()), 500)) {
            var application = new Application(
                    twsApi,
                    Clock.systemDefaultZone(),
                    configuration.fundamentalsRequest(),
                    configuration.cacheDirectory(),
                    configuration.priceRequest(),
                    HttpClient.newHttpClient(),
                    configuration.fmpApiKey(),
                    configuration.avApiKey()
            );
            print("Company name", application.companyName());
            print("Current price", application.price());
            var bookValue = application.bookValue();
            print("Book value (%s)".formatted(bookValue.date()), new Unchecked<>(bookValue.value()).value());
            print("EPS TTM", application.epsTtm());
            print("EPS 3 year average", application.epsAverage());
            print("Graham number", application.grahamNumber());
            print("Current price to Graham number", application.grahamRatio());
            print("Current ratio", application.currentRatio());
            print("Net current assets to long term debt ratio", application.netCurrentAssetsToLongTermDebtRatio());
            print("Net current asset value", application.netCurrentAssetValue());
            print("Net current asset value ratio", application.netCurrentAssetValueRatio());
            status = 0;
        } catch (ApplicationException e) {
            System.out.println("Error: " + e.getMessage());
            status = 1;
        } catch (RuntimeException e) {
            System.out.println("Unexpected error:");
            e.printStackTrace();
            status = 2;
        }
        // terminate the JVM, not just the current thread
        System.exit(status);
    }

    private static void print(String label, BigDecimal number) {
        print(label, "%f".formatted(number));
    }

    @SuppressWarnings({ "PMD.SystemPrintln", "java:S106" })
    private static void print(String label, String string) {
        System.out.printf("%s: %s%n", label, string);
    }
}
