package sk.ivankohut.quantifa;

public interface PriceRequest extends StockContract {

    String source();

    int divisor();
}
