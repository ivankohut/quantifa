package sk.ivankohut.quantifa;

import com.ib.client.Contract;
import com.ib.client.Types;
import com.ib.controller.ApiController;

import java.util.List;

public class TwsApiController implements AutoCloseable, TwsApi {

    private final ApiController apiController;

    /**
     * @param afterConnectionDelay - we have to wait some time, e.g. 0.5s (maybe for the inner threads to start), otherwise it does not work
     */
    public TwsApiController(TwsCoordinates coordinates, ApiController apiController, int afterConnectionDelay) {
        this.apiController = apiController;
        this.apiController.connect(coordinates.hostName(), coordinates.port(), 1, null);
        try {
            Thread.sleep(afterConnectionDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApplicationException(e);
        }
    }

    @Override
    public void close() {
        apiController.disconnect();
    }

    @Override
    public void setMarketDataType(int type) {
        apiController.reqMktDataType(type);
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
        apiController.reqTopMktData(contract, String.join(",", genericTicks), snapshot, regulatorySnapshot, handler);
    }

    @Override
    public void cancelTopMarketData(ApiController.ITopMktDataHandler handler) {
        apiController.cancelTopMktData(handler);
    }

    @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        var contract = new Contract();
        contract.secType(Types.SecType.STK);
        contract.exchange(stockContract.exchange());
        contract.symbol(stockContract.symbol());
        contract.currency(stockContract.currency());
        apiController.reqFundamentals(contract, type, handler);
    }
}
