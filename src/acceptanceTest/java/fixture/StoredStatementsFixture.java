package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.cactoos.iterable.Mapped;
import sk.ivankohut.quantifa.CachedFinancialStatementsTest;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.StockContract;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Setter
public class StoredStatementsFixture {

    private final List<String> storedStatementsDates;
    private LocalDate currentDate;

    private final String fundamentalsXml = IOUtils.resourceToString("/financialStatements.xml", StandardCharsets.UTF_8);

    public StoredStatementsFixture(List<String> storedStatementsDates) throws IOException {
        this.storedStatementsDates = storedStatementsDates;
    }

    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
    public boolean retrievingStatementsFromTWS() {
        resetCache();
        var twsApi = new TwsApiAdapter() {

            boolean fundamentalsRequested;

            @Override
            public void requestFundamentals(StockContract stockContract, Types.FundamentalType type, ApiController.IFundamentalsHandler handler) {
                handler.fundamentals(fundamentalsXml);
                fundamentalsRequested = true;
            }
        };
        new FixtureApplication(
                twsApi, CachedFinancialStatementsTest.clockFixedOn(currentDate), new SimpleStockContract("NYSE", "CAT", "USD")
        ).bookValue().value();
        return twsApi.fundamentalsRequested;
    }

    private void resetCache() {
        CacheUtils.resetWith(() -> "financialStatements/NYSE-CAT-USD", new Mapped<>(LocalDate::parse, storedStatementsDates), ".xml", fundamentalsXml);
    }
}
