package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import sk.ivankohut.quantifa.StockContract;
import sk.ivankohut.quantifa.TwsApi;

import java.util.List;

public class TwsApiAdapter implements TwsApi {

    @Override
    public void setMarketDataType(int type) {
        // empty
    }

    @Override
    public void requestTopMarketData(
            StockContract stockContract, List<String> genericTicks, boolean snapshot, boolean regulatorySnapshot, ApiController.ITopMktDataHandler handler
    ) {
        // empty
    }

    @Override
    public void cancelTopMarketData(ApiController.ITopMktDataHandler handler) {
        // empty
    }

    @Override
    public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
        // empty
    }
}
