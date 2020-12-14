package sk.ivankohut.quantifa;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class TwsFundamentalTest {

    @Test
    void returnsFundamentalsXmlTextFromTws() {
        var contract = mock(StockContract.class);
        var twsApi = mock(TwsApi.class);
        var fundamentalType = Types.FundamentalType.ReportsFinStatements;
        doAnswer(invocation -> {
            invocation.getArgument(2, ApiController.IFundamentalsHandler.class).fundamentals("fundamentals");
            return null;
        }).when(twsApi).requestFundamentals(eq(contract), eq(fundamentalType), any());
        var sut = new TwsFundamental(twsApi, contract, fundamentalType);
        // exercise
        var result = sut.asString();
        // verify
        assertThat(result).isEqualTo("fundamentals");
    }
}
