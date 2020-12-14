package fixture;

import com.ib.client.TickType;
import com.ib.controller.ApiController;
import sk.ivankohut.quantifa.StockContract;

import java.math.BigDecimal;
import java.util.List;

public class FakePriceMarketDataTwsApi extends TwsApiAdapter {

    private final TickType tickType;
    private final BigDecimal currentPriceInTWS;
    private final StockContract stockContract;

    public FakePriceMarketDataTwsApi(StockContract stockContract, TickType tickType, BigDecimal currentPriceInTWS) {
        this.stockContract = stockContract;
        this.tickType = tickType;
        this.currentPriceInTWS = currentPriceInTWS;
    }

    @Override
    public void requestTopMarketData(
            StockContract stockContract, List<String> genericTicks, boolean snapshot, boolean regulatorySnapshot, ApiController.ITopMktDataHandler handler
    ) {
        if (areStockContractsEqual(this.stockContract, stockContract)) {
            handler.tickPrice(tickType, currentPriceInTWS.doubleValue(), null);
        } else {
            throw new IllegalArgumentException("Expected arguments not provided.");
        }
    }

    public static boolean areStockContractsEqual(StockContract object, StockContract other) {
        return object.exchange().equals(other.exchange())
                && object.symbol().equals(other.symbol())
                && object.currency().equals(other.currency());
    }
}
