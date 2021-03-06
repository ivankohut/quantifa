package sk.ivankohut.quantifa;

import com.ib.client.Types;
import com.ib.controller.ApiController;

import java.util.List;

public interface TwsApi {

    void setMarketDataType(int type);

    void requestTopMarketData(
            StockContract stockContract,
            List<String> genericTicks,
            boolean snapshot,
            boolean regulatorySnapshot,
            ApiController.ITopMktDataHandler handler
    );

    void cancelTopMarketData(ApiController.ITopMktDataHandler handler);

    void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler);
}
