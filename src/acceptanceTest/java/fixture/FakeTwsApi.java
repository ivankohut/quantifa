package fixture;

import com.ib.client.TickType;
import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.cactoos.Text;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.StockContract;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FakeTwsApi extends TwsApiAdapter {

    private final Map<StockContract, Map<TickType, BigDecimal>> prices;
    private final StockContract stockContract;
    private final Text fundamentals;

    public FakeTwsApi(Map<StockContract, Map<TickType, BigDecimal>> prices, StockContract stockContract, Text fundamentals) {
        this.prices = new LinkedHashMap<>(prices);
        this.stockContract = stockContract;
        this.fundamentals = fundamentals;
    }

    public FakeTwsApi(StockContract stockContract, Text fundamentals) {
        this(Map.of(), stockContract, fundamentals);
    }

    public FakeTwsApi(Map<StockContract, Map<TickType, BigDecimal>> prices) {
        this(prices, new SimpleStockContract("", "", ""), new TextOf(""));
    }

    @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        if (areStockContractsEqual(this.stockContract, stockContract)) {
            handler.fundamentals(new UncheckedText(fundamentals).asString());
        } else {
            throw new IllegalArgumentException("Expected arguments not provided.");
        }
    }

    @Override
    public void requestTopMarketData(
            StockContract stockContract, List<String> genericTicks, boolean snapshot, boolean regulatorySnapshot, ApiController.ITopMktDataHandler handler
    ) {
        // wrapping stockContract to SimpleStockContract so that look up in map works
        prices.get(new SimpleStockContract(stockContract)).forEach((key, value) -> handler.tickPrice(key, value.doubleValue(), null));
    }

    public static boolean areStockContractsEqual(StockContract object, StockContract other) {
        return object.exchange().equals(other.exchange())
                && object.symbol().equals(other.symbol())
                && object.currency().equals(other.currency());
    }
}
