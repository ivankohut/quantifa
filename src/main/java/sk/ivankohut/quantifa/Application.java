package sk.ivankohut.quantifa;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.cactoos.Scalar;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.ScalarOfSupplier;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.Concatenated;
import org.cactoos.text.TextOf;
import org.json.JSONObject;
import sk.ivankohut.quantifa.decimal.DecimalOf;
import sk.ivankohut.quantifa.decimal.DivisionOf;
import sk.ivankohut.quantifa.decimal.MultiplicationOf;
import sk.ivankohut.quantifa.decimal.NonNegative;
import sk.ivankohut.quantifa.decimal.Rounded;
import sk.ivankohut.quantifa.decimal.SquareRootOf;
import sk.ivankohut.quantifa.decimal.SumOf;
import sk.ivankohut.quantifa.utils.ContentOfUri;
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

@SuppressWarnings("PMD.DataClass")
public class Application {

    private final MarketPrice price;
    private final ReportedAmount bookValue;
    private final Scalar<BigDecimal> epsTtm;
    private final Scalar<BigDecimal> epsAverage;
    private final Scalar<BigDecimal> grahamNumber;
    private final Scalar<BigDecimal> grahamRatio;
    private final Scalar<BigDecimal> currentRatio;
    private final Scalar<BigDecimal> netCurrentAssetsToLongTermDebtRatio;

    // checkstyle nor pmd does not properly support switch expression yet
    @SuppressWarnings({ "checkstyle:Indentation", "checkstyle:WhitespaceAround", "PMD.UselessParentheses" })
    public Application(TwsApi twsApi, Clock clock, StockContract fundamentalsRequest, Path cacheDirectory, PriceRequest priceRequest, HttpClient httpClient) {
        var today = LocalDate.now(clock);
        var timeout = Duration.ofSeconds(15);
        var priceFileName = new Concatenated(
                new TextOf(today, DateTimeFormatter.ISO_LOCAL_DATE),
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
                                                    new Concatenated(
                                                            "https://financialmodelingprep.com/api/v3/stock/list?apikey=",
                                                            priceRequest.apiKey()),
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
            default -> throw new IllegalArgumentException("Unknown price source.");
        }).price().map(p -> p.divide(BigDecimal.valueOf(priceRequest.divisor())));

        var financialStatementsNode = new StickyFirstOrFail<>(
                new XPathNodes(
                        new CachedFinancialStatements(
                                new TextFilesStore(cacheDirectory.resolve("financialStatements")),
                                today,
                                new org.cactoos.text.Joined("-", fundamentalsRequest.exchange(), fundamentalsRequest.symbol(), fundamentalsRequest.currency()),
                                new TwsFundamental(twsApi, fundamentalsRequest, Types.FundamentalType.ReportsFinStatements),
                                13,
                                ".xml"
                        ),
                        "/ReportFinancialStatements/FinancialStatements"),
                "No financial statements available."
        );
        var annual = new Unchecked<>(new MostRecentFinancialStatementNode(new StatementNodes(financialStatementsNode, true, true)));
        var interim = new Unchecked<>(new MostRecentFinancialStatementNode(new StatementNodes(financialStatementsNode, false, true)));
        var statementEntry = new Unchecked<>(new Sticky<>(
                new Ternary<>(
                        new ScalarOfSupplier<>(() -> interim.value().getKey().isAfter(annual.value().getKey())),
                        interim,
                        annual
                )
        ));
        var mostRecentBalanceSheet = new XmlFinancialStatement(() -> statementEntry.value().getKey(), () -> statementEntry.value().getValue());
        this.bookValue = new ReportedAmount() {

            @Override
            public LocalDate date() {
                return mostRecentBalanceSheet.date();
            }

            @Override
            public BigDecimal value() {
                return new Unchecked<>(new DivisionOf(
                        mostRecentBalanceSheet.value("QTLE"),
                        new SumOf(mostRecentBalanceSheet.value("QTCO"), mostRecentBalanceSheet.value("QTPO")),
                        new BigDecimal(-1)
                )).value();
            }
        };
        this.epsTtm = new TrailingTwelveMonths(new Mapped<>(
                statementNode -> new FinancialStatementAmount(new XmlFinancialStatement(new XmlStatementDate(statementNode), () -> statementNode), "VDES"),
                new StatementNodes(financialStatementsNode, false, false)
        ));
        this.epsAverage = new AverageOfTheMostRecent(
                new Mapped<>(
                        statementNode -> new FinancialStatementAmount(
                                new XmlFinancialStatement(new XmlStatementDate(statementNode), () -> statementNode),
                                "VDES"
                        ),
                        new StatementNodes(financialStatementsNode, true, false)
                ),
                3
        );
        this.currentRatio = new DivisionOf(() -> mostRecentBalanceSheet.value("ATCA"), () -> mostRecentBalanceSheet.value("LTCL"), BigDecimal.ZERO);
        this.netCurrentAssetsToLongTermDebtRatio = new DivisionOf(
                () -> mostRecentBalanceSheet.value("ATCA").subtract(mostRecentBalanceSheet.value("LTCL")),
                () -> mostRecentBalanceSheet.value("LTTD"),
                BigDecimal.ZERO
        );
        this.grahamNumber = new Sticky<>(new SquareRootOf(new MultiplicationOf(
                new DecimalOf(15 * 1.5), new NonNegative(epsAverage), new NonNegative(bookValue::value))
        ));
        this.grahamRatio = new DivisionOf(() -> price.price().orElse(BigDecimal.ZERO), grahamNumber, BigDecimal.ZERO);
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
                    HttpClient.newHttpClient()
            );
            System.out.printf("Current price: %f%n", application.price());
            var bookValue = application.bookValue();
            System.out.printf("Latest book value: %f (%s)%n", bookValue.value(), bookValue.date());
            System.out.printf("Diluted normalized EPS TTM: %f%n", application.epsTtm());
            System.out.printf("Diluted normalized EPS 3 year average: %f%n", application.epsAverage());
            System.out.printf("Graham number: %f%n", application.grahamNumber());
            System.out.printf("Current price to Graham number: %f%n", application.grahamRatio());
            System.out.printf("Current ratio: %f%n", application.currentRatio());
            System.out.printf("Net current assets to long term debt ratio: %f%n", application.netCurrentAssetsToLongTermDebtRatio());
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
}
