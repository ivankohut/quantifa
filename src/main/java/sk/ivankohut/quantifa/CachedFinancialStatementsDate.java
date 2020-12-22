package sk.ivankohut.quantifa;

import org.cactoos.Text;
import org.cactoos.iterable.IterableOf;
import org.cactoos.scalar.And;
import org.cactoos.scalar.Equals;
import org.cactoos.scalar.SumOfInt;
import org.cactoos.scalar.Ternary;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.EndsWith;
import org.cactoos.text.Sub;
import sk.ivankohut.quantifa.utils.DateTimeParseExceptionToIterable;
import sk.ivankohut.quantifa.utils.LengthOf;
import sk.ivankohut.quantifa.utils.LocalDateOf;

import java.time.LocalDate;
import java.util.Iterator;

public class CachedFinancialStatementsDate implements Iterable<LocalDate> {

    private final Unchecked<Iterable<LocalDate>> date;

    public CachedFinancialStatementsDate(Text fileName, String extension) {
        this.date = new Unchecked<>(new Ternary<>(
                new And(
                        new EndsWith(fileName, extension),
                        new Equals<>(new SumOfInt(() -> 10, new LengthOf(() -> extension)), new LengthOf(fileName))
                ),
                new DateTimeParseExceptionToIterable<>(new LocalDateOf(new Sub(fileName, 0, 10))),
                new IterableOf<>()
        ));
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return date.value().iterator();
    }
}
