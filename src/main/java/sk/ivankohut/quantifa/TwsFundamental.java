package sk.ivankohut.quantifa;

import com.ib.client.Types;
import org.cactoos.Text;
import sk.ivankohut.quantifa.utils.UncheckedCompletableFuture;

public class TwsFundamental implements Text {

    private final TwsApi twsApi;
    private final StockContract stockContract;
    private final Types.FundamentalType type;

    public TwsFundamental(TwsApi twsApi, StockContract stockContract, Types.FundamentalType type) {
        this.twsApi = twsApi;
        this.stockContract = stockContract;
        this.type = type;
    }

    @Override
    public String asString() {
        var result = new UncheckedCompletableFuture<String>(5000);
        twsApi.requestFundamentals(stockContract, type, result::complete);
        return result.get();
    }
}
