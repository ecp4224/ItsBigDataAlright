package me.eddiep.bigdata.data.facebook;

public abstract class FBResponse<T> {
    private Paging paging;

    protected FBResponse() { }

    public abstract T getData();

    public Paging getPaging() {
        return paging;
    }
}
