package sk.ivankohut.quantifa;

import com.ib.controller.ApiController;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

public class Application {

    private final MarketPrice price;
    private final Unchecked<ReportedAmount> bookValue;

    public Application(TwsApi twsApi) {
        var stockContract = new SimpleStockContract("NYSE", "CAT", "USD");
        this.price = new TwsMarketPriceOfStock(twsApi, stockContract, true);
        this.bookValue = new Unchecked<>(new Sticky<>(new TwsBookValueOfStock(twsApi, stockContract)));
    }

    public MarketPrice price() {
        return price;
    }

    public ReportedAmount bookValue() {
        return bookValue.value();
    }

    @SuppressWarnings({ "PMD.SystemPrintln", "java:S106", "PMD.AvoidPrintStackTrace", "java:S4507", "PMD.AvoidCatchingGenericException" })
    public static void main(String[] args) {
        int status;
        try (var twsApi = new TwsApiController("localhost", 7496, new ApiController(new TwsConnectionHandler()), 500)) {
            var application = new Application(twsApi);
            System.out.printf("Current price: %s%n", application.price().price().map(Object::toString).orElse(""));
            var bookValue = application.bookValue();
            System.out.printf("Latest book value: %s (%s)%n", bookValue.value(), bookValue.date());
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
