package me.eddiep.bigdata.util;

import okhttp3.Call;

public class OkHttpCancelableWrapper implements Cancelable {
    private Call call;

    OkHttpCancelableWrapper(Call call) {
        this.call = call;
    }


    public void cancel() {
        call.cancel();
    }

    public boolean isCanceled() {
        return call.isCanceled();
    }
}
