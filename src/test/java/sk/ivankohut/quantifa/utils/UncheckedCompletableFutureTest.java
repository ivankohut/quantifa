package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UncheckedCompletableFutureTest {

    @Test
    void providesGiveCompletionValue() {
        var sut = new UncheckedCompletableFuture<Integer>();
        var value = 1;
        // exercise
        sut.complete(value);
        var result = sut.get();
        // verify
        assertThat(result).isEqualTo(value);
    }

    @Test
    void executionExceptionWrappedToRuntimeException() {
        var future = new CompletableFuture<Integer>();
        var rootCause = new IllegalStateException();
        future.completeExceptionally(rootCause);
        var sut = new UncheckedCompletableFuture<>(future);
        // exercise
        // verify
        assertThatThrownBy(sut::get)
                .isInstanceOf(RuntimeException.class)
                .hasCauseExactlyInstanceOf(ExecutionException.class)
                .hasRootCause(rootCause);
    }
}
