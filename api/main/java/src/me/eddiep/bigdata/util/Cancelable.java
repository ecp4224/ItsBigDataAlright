package me.eddiep.bigdata.util;

import okhttp3.Call;

public interface Cancelable {
    void cancel();

    boolean isCanceled();

    public static Cancelable from(Call call) {
        return new OkHttpCancelableWrapper(call);
    }
}
