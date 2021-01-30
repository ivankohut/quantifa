package sk.ivankohut.quantifa;

public interface PriceRequest extends StockContract {

    String source();

    String apiKey();

    int divisor();
}
