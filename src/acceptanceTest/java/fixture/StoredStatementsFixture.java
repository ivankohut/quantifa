package fixture;

import com.ib.client.Types;
import com.ib.controller.ApiController;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cactoos.Text;
import org.cactoos.iterable.Mapped;
import sk.ivankohut.quantifa.Application;
import sk.ivankohut.quantifa.CachedFinancialStatementsTest;
import sk.ivankohut.quantifa.TextFilesStore;
import sk.ivankohut.quantifa.StockContract;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
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
        new Application(twsApi, CachedFinancialStatementsTest.clockFixedOn(currentDate)).bookValue();
        return twsApi.fundamentalsRequested;
    }

    private void resetCache() {
        resetCache(() -> "NYSE-CAT-USD", new Mapped<>(LocalDate::parse, storedStatementsDates), fundamentalsXml);
    }

    public static void resetCache(Text directory, Iterable<LocalDate> fileNames, String fundamentalsXml) {
        clearCache();
        var store = new TextFilesStore("financialStatementsCache");
        fileNames.forEach(date -> store.newFile(directory, date + ".xml", fundamentalsXml));
    }

    public static void clearCache() {
        try {
            FileUtils.deleteDirectory(new File("financialStatementsCache"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
