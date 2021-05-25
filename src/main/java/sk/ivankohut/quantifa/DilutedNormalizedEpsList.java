package sk.ivankohut.quantifa;

import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.Mapped;

public class DilutedNormalizedEpsList extends IterableEnvelope<FinancialStatementAmount> {

    public DilutedNormalizedEpsList(Iterable<FiscalPeriod> periods) {
        super(new Mapped<>(
                period -> new FinancialStatementAmount(period.incomeStatement(), "VDES"),
                periods
        ));
    }
}
