package sk.ivankohut.quantifa.utils;

import org.junit.jupiter.api.Test;
import sk.ivankohut.quantifa.ApplicationException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UncheckedCompletableFutureTest {

    @Test
    void providesGivenCompletionValue() {
        var sut = new UncheckedCompletableFuture<Integer>(10_000);
        var value = 1;
        // exercise
        sut.complete(value);
        var result = sut.get();
        // verify
        assertThat(result).isEqualTo(value);
    }

    @Test
    void interruptWrappedIntoRuntimeException() throws ExecutionException, InterruptedException, TimeoutException {
        var cause = new InterruptedException();
        var future = when(mock(CompletableFuture.class).get(1, TimeUnit.MILLISECONDS)).thenThrow(cause).<CompletableFuture<Integer>>getMock();
        var sut = new UncheckedCompletableFuture<>(future, 1);
        // exercise
        // verify
        assertThatThrownBy(sut::get)
                .isExactlyInstanceOf(RuntimeException.class)
                .hasCause(cause);
    }

    @Test
    void executionExceptionWrappedToRuntimeException() {
        var future = new CompletableFuture<Integer>();
        var rootCause = new IllegalStateException();
        future.completeExceptionally(rootCause);
        var sut = new UncheckedCompletableFuture<>(future, 10_000);
        // exercise
        // verify
        assertThatThrownBy(sut::get)
                .isInstanceOf(ApplicationException.class)
                .hasCauseExactlyInstanceOf(ExecutionException.class)
                .hasRootCause(rootCause);
    }

    @Test
    void timeoutWrappedToApplicationException() {
        var future = new CompletableFuture<Integer>();
        var sut = new UncheckedCompletableFuture<>(future, 1);
        // exercise
        // verify
        assertThatThrownBy(sut::get)
                .isInstanceOf(ApplicationException.class)
                .hasCauseExactlyInstanceOf(TimeoutException.class);
    }
}
