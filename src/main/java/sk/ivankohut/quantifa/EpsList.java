package sk.ivankohut.quantifa;

import org.cactoos.iterable.IterableEnvelope;
import org.cactoos.iterable.Mapped;

public class EpsList extends IterableEnvelope<FinancialStatementAmount> {

    public EpsList(Iterable<FiscalPeriod> periods) {
        super(new Mapped<>(
                period -> new FinancialStatementAmount(period.incomeStatement(), "SDBF"),
                periods
        ));
    }
}
