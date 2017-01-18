package me.eddiep.bigdata.data.facebook.impl;

import me.eddiep.bigdata.data.facebook.FBResponse;

public class FBReactionResponse extends FBResponse<FBReaction[]> {
    private FBReaction[] data;

    private FBReactionResponse() { }

    public FBReaction[] getData() {
        return data;
    }
}
