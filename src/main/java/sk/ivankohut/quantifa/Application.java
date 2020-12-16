package sk.ivankohut.quantifa;

import com.ib.controller.ApiController;

public class Application {

    private final MarketPrice price;

    public Application(TwsApi twsApi) {
        this.price = new TwsMarketPriceOfStock(twsApi, new SimpleStockContract("NYSE", "CAT", "USD"), true);
    }

    @Override
    public String toString() {
        return price.price().map(Object::toString).orElse("");
    }

    @SuppressWarnings({ "PMD.SystemPrintln", "java:S106", "PMD.AvoidPrintStackTrace", "java:S4507", "PMD.AvoidCatchingGenericException" })
    public static void main(String[] args) {
        int status;
        try (var twsApi = new TwsApiController("localhost", 7496, new ApiController(new TwsConnectionHandler()), 500)) {
            System.out.println(new Application(twsApi).toString());
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
