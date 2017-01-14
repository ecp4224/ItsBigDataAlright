package me.eddiep.bigdata.data.video;

import org.jetbrains.annotations.NotNull;

public enum VideoType {
    YOUTUBE("yt"),
    WEBM("webm"),
    UNKNOWN("unknown");

    String type;
    VideoType(String type) { this.type = type; }
    public String getType() {
        return type;
    }

    @NotNull
    public static VideoType parse(@NotNull String type) {
        for (VideoType t : values()) {
            if (t.type.equals(type))
                return t;
        }
        return UNKNOWN;
    }
}
