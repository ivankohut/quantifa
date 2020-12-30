package sk.ivankohut.quantifa;

import org.cactoos.Text;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CachedFinancialStatementsTest {

    @Test
    void providesLatestFundamentalsFromCache() throws Exception {
        var fundamentals = "fundamentals";
        var directory = new TextOf("NYSE-CAT-USD");
        var store = createStore(
                directory,
                new SimpleTextFile("2020-05-24-3.xml", fundamentals + "X"),
                new SimpleTextFile("2020-05-25-3.xml", fundamentals),
                new SimpleTextFile("2020-05-23-3.xml", fundamentals + "X")
        );
        var sut = create(store, "2020-10-23", directory, () -> "", 2, ".xml");
        // exercise
        var result = sut.asString();
        // verify
        assertThat(result).isEqualTo(fundamentals);
    }

    @Test
    void callsTwsAndStoresFundamentalsInCacheIfNoCacheEntryIsOldGivenNumberOfMonthsOrLess() throws Exception {
        var fundamentals = """
                <?xml version="1.0" encoding="UTF-8"?>
                <ReportFinancialStatements Major="1" Minor="0" Revision="1">
                    <CoGeneralInfo>
                        <LatestAvailableInterim>2020-09-30</LatestAvailableInterim>
                    </CoGeneralInfo>
                    <FinancialStatements>
                        <InterimPeriods>
                            <FiscalPeriod Type="Interim" EndDate="2020-09-30" FiscalYear="2020" FiscalPeriodNumber="3">
                                <Statement Type="INC">
                                    <FPHeader>
                                        <PeriodLength>3</PeriodLength>
                                    </FPHeader>
                                </Statement>
                            </FiscalPeriod>
                        </InterimPeriods>
                    </FinancialStatements>
                </ReportFinancialStatements>
                """;
        var directory = new TextOf("NYSE-CAT-USD");
        var store = createStore(directory, new SimpleTextFile("2020-05-25-3.xml", "cachedFundamentals"));
        var sut = create(store, "2020-10-26", directory, () -> fundamentals, 2, ".xml");
        // exercise
        var result = sut.asString();
        // verify
        assertThat(result).isEqualTo(fundamentals);
        verify(store).newFile(directory, "2020-09-30-3.xml", fundamentals);
    }

    private static CachedFinancialStatements create(
            Store store,
            String currentDate,
            Text directory,
            Text fundamental,
            int outdatedAfterMonths,
            String fileExtension
    ) {
        return new CachedFinancialStatements(
                store,
                clockFixedOn(LocalDate.parse(currentDate)),
                directory,
                fundamental,
                outdatedAfterMonths,
                fileExtension
        );
    }

    public static Clock clockFixedOn(LocalDate date) {
        return Clock.fixed(date.atStartOfDay().atZone(ZoneOffset.systemDefault()).toInstant(), ZoneOffset.systemDefault());
    }

    private static Store createStore(Text directory, TextFile... textFiles) {
        return when(mock(Store.class).files(directory)).thenReturn(List.of(textFiles)).getMock();
    }
}
