package sk.ivankohut.quantifa.utils;

import org.cactoos.Proc;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PeekedScalarTest {

    @Test
    void providedGivenValueAndExecutesGiveProcedure() throws Exception {
        @SuppressWarnings("unchecked")
        Proc<Integer> proc = mock(Proc.class);
        var sut = new PeekedScalar<>(() -> 1, proc);
        // exercise
        var result = sut.value();
        // verify
        assertThat(result).isEqualTo(1);
        verify(proc).exec(1);
    }
}
