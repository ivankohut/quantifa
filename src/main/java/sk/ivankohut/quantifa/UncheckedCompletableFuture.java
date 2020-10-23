package sk.ivankohut.quantifa;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class UncheckedCompletableFuture<T> implements Supplier<T> {

    private final CompletableFuture<T> future;

    public UncheckedCompletableFuture(CompletableFuture<T> future) {
        this.future = future;
    }

    public UncheckedCompletableFuture() {
        this(new CompletableFuture<>());
    }

    public void complete(T value) {
        future.complete(value);
    }

    @Override
    public T get() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApplicationException(e);
        }
    }
}
