package me.eddiep.bigdata.data.fetch;

import me.eddiep.bigdata.data.VideoStore;
import me.eddiep.bigdata.data.video.Video;
import me.eddiep.bigdata.util.ArrayHelper;
import me.eddiep.bigdata.util.Cancelable;
import me.eddiep.bigdata.util.PRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class FetchTask implements Cancelable {
    private List<Fetcher> fetchers = new ArrayList<>();
    private List<Future<List<Video>>> futures = new ArrayList<>();
    private Thread fetchThread;
    private boolean isRunning;

    public void addFetcher(Fetcher fetcher) {
        if (!fetchers.contains(fetcher))
            fetchers.add(fetcher);
    }

    public void start() {
        isRunning = true;
        fetchThread = new Thread(FETCH_RUNNABLE);
        fetchThread.start();
    }

    private final Runnable FETCH_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                for (Fetcher f : fetchers) {
                    Future<List<Video>> future = f.fetch();

                    futures.add(future);
                }

                ArrayHelper.combindAsync(futures, p -> {
                    VideoStore.saveVideos(p);
                    futures.clear();
                });

                while (!futures.isEmpty()) {
                    try {
                        Thread.sleep(2 * 60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void cancel() {
        this.isRunning = false;
        for (int i = 0; i < futures.size(); i++) {
            Future f = futures.get(i);
            f.cancel(true);
        }
        futures.clear();
        fetchThread.interrupt();
    }

    @Override
    public boolean isCanceled() {
        return !this.isRunning;
    }
}
