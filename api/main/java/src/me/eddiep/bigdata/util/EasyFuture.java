package me.eddiep.bigdata.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EasyFuture<T> implements Future<T> {
    private Cancelable cancelable;
    private T value;

    public EasyFuture(Cancelable cancelable) {
        this.cancelable = cancelable;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        if (value != null)
            return false;

        cancelable.cancel();

        return cancelable.isCanceled();
    }

    public boolean isCancelled() {
        return value == null && cancelable.isCanceled();
    }

    public boolean isDone() {
        return value != null;
    }

    public synchronized void setValue(T value) {
        if (isCancelled())
            return;
        this.value = value;
        super.notifyAll();
    }

    public synchronized T get() throws InterruptedException, ExecutionException {
        while (true) {
            if (value != null)
                break;
            super.wait(0L);
        }

        return value;
    }

    public synchronized T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (value != null)
            return value;

        super.wait(unit.toMillis(timeout));

        if (value == null)
            throw new TimeoutException();
        return value;
    }
}
