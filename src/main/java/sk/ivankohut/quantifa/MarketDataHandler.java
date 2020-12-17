package sk.ivankohut.quantifa;

import com.ib.client.TickAttrib;
import com.ib.client.TickType;
import com.ib.controller.ApiController;
import lombok.RequiredArgsConstructor;
import sk.ivankohut.quantifa.utils.UncheckedCompletableFuture;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class MarketDataHandler extends ApiController.TopMktDataAdapter implements MarketPrice {

    private final boolean bid;
    private final UncheckedCompletableFuture<Optional<BigDecimal>> result = new UncheckedCompletableFuture<>();

    @Override
    public void tickPrice(TickType tickType, double price, TickAttrib attribs) {
        var interestingTickType = bid ? TickType.DELAYED_BID : TickType.DELAYED_ASK;
        if (tickType == interestingTickType) {
            result.complete(price == -1.0 ? Optional.empty() : Optional.of(BigDecimal.valueOf(price)));
        }
    }

    @Override
    public Optional<BigDecimal> price() {
        return result.get();
    }
}
