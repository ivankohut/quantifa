package fixture;

import com.ib.client.TickType;
import lombok.Setter;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.SimpleStockContract;

import java.math.BigDecimal;

@Setter
public class NyseCatPriceFixture {

    private String exchange;
    private String symbol;
    private BigDecimal priceInTWS;
    private String currency;
    private TickType tickType;

    public BigDecimal price() {
        var application = new Application(new FakePriceMarketDataTwsApi(new SimpleStockContract(exchange, symbol, currency), tickType, priceInTWS));
        return new BigDecimal(application.toString());
    }
}
