package me.eddiep.bigdata.util;

public class CancelToken implements Cancelable {
    private boolean isCanceled;

    @Override
    public void cancel() {
        isCanceled = true;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }
}
