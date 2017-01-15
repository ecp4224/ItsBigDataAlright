package me.eddiep.bigdata.data.fetch;

import me.eddiep.bigdata.data.video.Video;

import java.util.List;
import java.util.concurrent.Future;

public interface Fetcher {

    Future<List<Video>> fetch();
}
