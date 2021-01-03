package sk.ivankohut.quantifa;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.cactoos.Scalar;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.ScalarOfSupplier;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import sk.ivankohut.quantifa.decimal.DivisionOf;
import sk.ivankohut.quantifa.decimal.SumOf;
import sk.ivankohut.quantifa.utils.StickyFirstOrFail;
import sk.ivankohut.quantifa.xmldom.XPathNodes;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

@SuppressWarnings("PMD.DataClass")
public class Application {

    private final MarketPrice price;
    private final ReportedAmount bookValue;
    private final Scalar<BigDecimal> epsTtm;
    private final Scalar<BigDecimal> epsAverage;

    public Application(TwsApi twsApi, Clock clock, StockContract stockContract) {
        this.price = new TwsMarketPriceOfStock(twsApi, stockContract, true);
        var financialStatementsNode = new StickyFirstOrFail<>(
                new XPathNodes(
                        new CachedFinancialStatements(
                                new TextFilesStore("financialStatementsCache"),
                                clock,
                                new org.cactoos.text.Joined("-", stockContract.exchange(), stockContract.symbol(), stockContract.currency()),
                                new TwsFundamental(twsApi, stockContract, Types.FundamentalType.ReportsFinStatements),
                                5,
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
                        new SumOf(mostRecentBalanceSheet.value("QTCO"), mostRecentBalanceSheet.value("QTPO"))
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
    }

    public Application(TwsApi twsApi, StockContract stockContract) {
        this(twsApi, Clock.systemDefaultZone(), stockContract);
    }

    public MarketPrice price() {
        return price;
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

    @SuppressWarnings({ "PMD.SystemPrintln", "java:S106", "PMD.AvoidPrintStackTrace", "java:S4507", "PMD.AvoidCatchingGenericException" })
    public static void main(String[] args) {
        int status;
        var configuration = new ApplicationConfiguration("environment variable", System.getenv());
        try (var twsApi = new TwsApiController(configuration, new ApiController(new TwsConnectionHandler()), 500)) {
            var application = new Application(twsApi, configuration);
            System.out.printf("Current price: %f%n", application.price().price().orElse(BigDecimal.ZERO));
            var bookValue = application.bookValue();
            System.out.printf("Latest book value: %f (%s)%n", bookValue.value(), bookValue.date());
            System.out.printf("Diluted normalized EPS TTM: %f%n", application.epsTtm());
            System.out.printf("Diluted normalized EPS 3 year average: %f%n", application.epsAverage());
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
