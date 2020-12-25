package fixture;

import com.ib.client.TickType;
import com.ib.controller.ApiController;
import sk.ivankohut.quantifa.StockContract;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class FakePriceMarketDataTwsApi extends TwsApiAdapter {

    private final Map<StockContract, Map<TickType, BigDecimal>> prices;

    public FakePriceMarketDataTwsApi(Map<StockContract, Map<TickType, BigDecimal>> prices) {
        this.prices = prices;
    }

    @Override
    public void requestTopMarketData(
            StockContract stockContract, List<String> genericTicks, boolean snapshot, boolean regulatorySnapshot, ApiController.ITopMktDataHandler handler
    ) {
        prices.get(stockContract).forEach((key, value) -> handler.tickPrice(key, value.doubleValue(), null));
    }

    public static boolean areStockContractsEqual(StockContract object, StockContract other) {
        return object.exchange().equals(other.exchange())
                && object.symbol().equals(other.symbol())
                && object.currency().equals(other.currency());
    }
}
