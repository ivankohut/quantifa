package sk.ivankohut.quantifa.utils;

import sk.ivankohut.quantifa.ApplicationException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class UncheckedCompletableFuture<T> implements Supplier<T> {

    private final CompletableFuture<T> future;
    private final long timeoutInMillis;

    public UncheckedCompletableFuture(CompletableFuture<T> future, long timeoutInMillis) {
        this.future = future;
        this.timeoutInMillis = timeoutInMillis;
    }

    public UncheckedCompletableFuture(long timeoutInMillis) {
        this(new CompletableFuture<>(), timeoutInMillis);
    }

    public void complete(T value) {
        future.complete(value);
    }

    @Override
    @SuppressWarnings({ "PMD.AvoidThrowingRawExceptionTypes", "java:S112" })
    public T get() {
        try {
            return future.get(timeoutInMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException | TimeoutException e) {
            throw new ApplicationException(e);
        }
    }
}
