package me.eddiep.bigdata.data.facebook.impl;

import me.eddiep.bigdata.data.facebook.FBResponse;

public class FBFeedResponse extends FBResponse<FeedItem[]> {
    private FeedItem[] data;

    public FeedItem[] getData() {
        return data;
    }
}
