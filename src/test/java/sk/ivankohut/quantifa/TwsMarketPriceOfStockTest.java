package sk.ivankohut.quantifa;

import com.ib.client.MarketDataType;
import com.ib.client.TickType;
import com.ib.controller.ApiController;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TwsMarketPriceOfStockTest {

    @Test
    void returnsPriceReturnedByTws() {
        var price = BigDecimal.ONE;
        var contract = mock(StockContract.class);
        var handlerHolder = new Holder<ApiController.ITopMktDataHandler>();
        var twsApi = mock(TwsApi.class);
        doAnswer(invocation -> {
            var handler = invocation.getArgument(4, ApiController.ITopMktDataHandler.class);
            handler.tickPrice(TickType.DELAYED_BID, price.doubleValue(), null);
            handlerHolder.set(handler);
            return null;
        }).when(twsApi).requestTopMarketData(eq(contract), eq(List.of()), eq(false), eq(false), any());
        var sut = new TwsMarketPriceOfStock(twsApi, contract, true);
        // exercise
        var result = sut.price();
        // verify
        verify(twsApi).setMarketDataType(MarketDataType.DELAYED);
        verify(twsApi).cancelTopMarketData(handlerHolder.get());
        assertThat(result.get()).isEqualByComparingTo(price);
    }
}
