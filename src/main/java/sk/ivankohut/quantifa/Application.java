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

    @SuppressWarnings({"PMD.SystemPrintln", "java:S106"})
    public static void main(String[] args) {
        try (var twsApi = new TwsApiController("localhost", 7496, new ApiController(new TwsConnectionHandler()), 500)) {
            System.out.println(new Application(twsApi).toString());
        }
    }
}
