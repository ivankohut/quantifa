package sk.ivankohut.quantifa;

import com.ib.client.MarketDataType;
import com.ib.client.TickType;
import com.ib.controller.ApiController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static sk.ivankohut.quantifa.MockitoUtils.doVoidAnswer;

class TwsMarketPriceOfStockTest {

    @Test
    void returnsPriceReturnedByTws() {
        var price = BigDecimal.ONE;
        var contract = mock(StockContract.class);
        var twsApi = mock(TwsApi.class);
        var captor = ArgumentCaptor.forClass(ApiController.ITopMktDataHandler.class);
        doVoidAnswer(invocation -> invocation.getArgument(4, ApiController.ITopMktDataHandler.class).tickPrice(TickType.DELAYED_BID, price.doubleValue(), null))
                .when(twsApi).requestTopMarketData(eq(contract), eq(List.of()), eq(false), eq(false), captor.capture());
        var sut = new TwsMarketPriceOfStock(twsApi, contract, true);
        // exercise
        var result = sut.price();
        // verify
        verify(twsApi).setMarketDataType(MarketDataType.DELAYED);
        verify(twsApi).cancelTopMarketData(captor.getValue());
        assertThat(result.get()).isEqualByComparingTo("1.0");
    }
}
