package sk.ivankohut.quantifa;

import com.ib.client.Contract;
import com.ib.client.MarketDataType;
import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TwsApiControllerTest {

    @Test
    void connectsDuringCreationAndDisconnectWhenClosing() {
        var apiController = mock(ApiController.class);
        var host = "host";
        var port = 1;
        // exercise
        new TwsApiController(host, port, apiController, 0);
        // verify
        verify(apiController).connect(host, port, 1, null);
    }

    @Test
    void disconnectsWhenClosing() {
        var apiController = mock(ApiController.class);
        var sut = create(apiController);
        // exercise
        sut.close();
        // verify
        verify(apiController).disconnect();
    }

    @Test
    void delegatesMarketDataTypeSettingToTws() {
        var apiController = mock(ApiController.class);
        var sut = create(apiController);
        var marketDataType = MarketDataType.FROZEN;
        // exercise
        sut.setMarketDataType(marketDataType);
        // verify
        verify(apiController).reqMktDataType(marketDataType);
    }

    @Test
    void delegatesMarketPriceRequestToTws() {
        var apiController = mock(ApiController.class);
        var sut = create(apiController);
        var handler = mock(ApiController.ITopMktDataHandler.class);
        var stockContract = new SimpleStockContract("exchange", "symbol", "currency");
        // exercise
        sut.requestTopMarketData(stockContract, List.of("tick1", "tick2"), false, true, handler);
        // verify
        var argumentCaptor = ArgumentCaptor.forClass(Contract.class);
        verify(apiController).reqTopMktData(argumentCaptor.capture(), eq("tick1,tick2"), eq(false), eq(true), eq(handler));
        var contract = argumentCaptor.getValue();
        assertThat(contract.exchange()).isEqualTo(stockContract.exchange());
        assertThat(contract.symbol()).isEqualTo(stockContract.symbol());
        assertThat(contract.currency()).isEqualTo(stockContract.currency());
        assertThat(contract.secType()).isEqualTo(Types.SecType.STK);
    }

    @Test
    void delegatesMarketPriceCancellationRequestToTws() {
        var apiController = mock(ApiController.class);
        var sut = create(apiController);
        var handler = mock(ApiController.ITopMktDataHandler.class);
        // exercise
        sut.cancelTopMarketData(handler);
        // verify
        verify(apiController).cancelTopMktData(handler);
    }

    private static TwsApiController create(ApiController apiController) {
        return new TwsApiController("host", 1, apiController, 0);
    }
}
