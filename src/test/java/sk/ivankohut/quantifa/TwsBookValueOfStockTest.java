package sk.ivankohut.quantifa;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static sk.ivankohut.quantifa.MockitoUtils.doVoidAnswer;

class TwsBookValueOfStockTest {

    @Test
    void returnsBookValueFromTheLatestBalanceSheetStatementReturnedByTws() throws Exception {
        var contract = mock(StockContract.class);
        var twsApi = mock(TwsApi.class);
        doVoidAnswer(invocation -> invocation.getArgument(2, ApiController.IFundamentalsHandler.class)
                    .fundamentals(IOUtils.resourceToString("/financialStatements.xml", StandardCharsets.UTF_8))
        ).when(twsApi).requestFundamentals(eq(contract), eq(Types.FundamentalType.ReportsFinStatements), any());
        var sut = new TwsBookValueOfStock(twsApi, contract);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result.value()).isEqualByComparingTo(new BigDecimal("13.404310"));
        assertThat(result.date()).isEqualTo("2020-09-30");
    }
}
