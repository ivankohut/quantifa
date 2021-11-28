package sk.ivankohut.quantifa;

import com.ib.client.Contract;
import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;
import sk.ivankohut.quantifa.utils.DelayedScalar;

import java.util.List;

public class TwsApiController implements AutoCloseable, TwsApi {

    private final Unchecked<ApiController> apiController;
    private boolean connected;

    /**
     * @param afterConnectionDelay - we have to wait some time, e.g. 0.5s (maybe for the inner threads to start), otherwise it does not work
     */
    public TwsApiController(TwsCoordinates coordinates, ApiController apiController, int afterConnectionDelay) {
        this.apiController = new Unchecked<>(new Sticky<>(new DelayedScalar<>(
                () -> {
                    apiController.connect(coordinates.hostName(), coordinates.port(), 1, null);
                    connected = true;
                    return apiController;
                },
                afterConnectionDelay
        )));
    }

    @Override
    public void close() {
        if (connected) {
            apiController.value().disconnect();
        }
    }

    @Override
    public void setMarketDataType(int type) {
        apiController.value().reqMktDataType(type);
    }

    @Override
    public void requestTopMarketData(
            StockContract stockContract, List<String> genericTicks, boolean snapshot, boolean regulatorySnapshot, ApiController.ITopMktDataHandler handler
    ) {
        var contract = new Contract();
        contract.secType(Types.SecType.STK);
        contract.exchange(stockContract.exchange());
        contract.symbol(stockContract.symbol());
        contract.currency(stockContract.currency());
        apiController.value().reqTopMktData(contract, String.join(",", genericTicks), snapshot, regulatorySnapshot, handler);
    }

    @Override
    public void cancelTopMarketData(ApiController.ITopMktDataHandler handler) {
        apiController.value().cancelTopMktData(handler);
    }

    @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        var contract = new Contract();
        contract.secType(Types.SecType.STK);
        contract.exchange(stockContract.exchange());
        contract.symbol(stockContract.symbol());
        contract.currency(stockContract.currency());
        apiController.value().reqFundamentals(contract, type, handler);
    }
}
