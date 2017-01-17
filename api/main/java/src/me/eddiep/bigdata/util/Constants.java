package me.eddiep.bigdata.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

public class Constants {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static final long GROUP_ID = 210065222796783L;

    public static final String FB_EMBED_URL = "https://www.facebook.com/video/embed?video_id=";

    public static final String ACCESS_TOKEN = "1234339043288284|wZmF-d-EfY66b9fZlerIn_iSK3g";
    @NotNull
    public static final OkHttpClient CLIENT = new OkHttpClient();
}
