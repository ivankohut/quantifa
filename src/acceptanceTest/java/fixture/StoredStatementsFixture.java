package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import sk.ivankohut.quantifa.CachedFinancialStatementsTest;
import sk.ivankohut.quantifa.SimpleStockContract;
import sk.ivankohut.quantifa.StockContract;
import sk.ivankohut.quantifa.TextFilesStore;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Setter
public class StoredStatementsFixture {

    public static final Path CACHE_DIRECTORY = Path.of("build/tmp/acceptanceTestCache");

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
        resetCache(() -> "NYSE-CAT-USD", new Mapped<>(LocalDate::parse, storedStatementsDates), fundamentalsXml);
    }

    public static void resetCache(Text directory, Iterable<LocalDate> fileNames, String fundamentalsXml) {
        clearCache();
        var store = new TextFilesStore(CACHE_DIRECTORY);
        fileNames.forEach(date -> store.newFile(directory, date + ".xml", fundamentalsXml));
    }

    public static void clearCache() {
        try {
            FileUtils.deleteDirectory(CACHE_DIRECTORY.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
