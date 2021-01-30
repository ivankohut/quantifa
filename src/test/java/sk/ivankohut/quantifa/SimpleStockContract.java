package sk.ivankohut.quantifa;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class SimpleStockContract implements StockContract {

    private final String exchange;
    private final String symbol;
    private final String currency;

    public SimpleStockContract(StockContract stockContract) {
        this(stockContract.exchange(), stockContract.symbol(), stockContract.currency());
    }

    @Override
    public String exchange() {
        return exchange;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public String currency() {
        return currency;
    }
}
