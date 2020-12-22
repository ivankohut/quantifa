package sk.ivankohut.quantifa;

import org.cactoos.Text;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Joined;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.Sorted;
import org.cactoos.scalar.FirstOf;
import org.cactoos.text.TextEnvelope;
import org.cactoos.text.TextOf;
import sk.ivankohut.quantifa.utils.PeekedScalar;
import sk.ivankohut.quantifa.xmldom.XPathNodes;
import sk.ivankohut.quantifa.xmldom.XmlDocument;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;

public class CachedFinancialStatements extends TextEnvelope {

    // probably bug in SonarQube rule (and in IDEA as well)
    @SuppressWarnings("java:S2293")
    public CachedFinancialStatements(Store store, Clock clock, Text directory, Text fundamental, int outdatedAfterMonths, String fileExtension) {
        super(new TextOf(new FirstOf<String>(
                new Mapped<>(
                        e -> e.getValue().content().asString(),
                        new Sorted<>(
                                (a, b) -> b.getKey().compareTo(a.getKey()),
                                new Filtered<>(
                                        e -> !e.getKey().plusMonths(outdatedAfterMonths).isBefore(LocalDate.now(clock)),
                                        new Joined<Map.Entry<LocalDate, TextFile>>(
                                                new Mapped<>(
                                                        textFile -> new Mapped<>(
                                                                date -> Map.entry(date, textFile),
                                                                new CachedFinancialStatementsDate(textFile::name, fileExtension)
                                                        ),
                                                        store.files(directory)
                                                )
                                        )
                                )
                        )
                ),
                new PeekedScalar<>(
                        fundamental::asString,
                        content -> store.newFile(
                                directory,
                                new XPathNodes(new XmlDocument(() -> content), "/ReportFinancialStatements/CoGeneralInfo/LatestAvailableInterim")
                                        .iterator().next().getTextContent() + fileExtension,
                                content
                        )
                )
        )));
    }
}
