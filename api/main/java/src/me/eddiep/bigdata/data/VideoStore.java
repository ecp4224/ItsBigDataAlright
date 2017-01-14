package me.eddiep.bigdata.data;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptionDefaults;
import me.eddiep.bigdata.data.video.Video;
import me.eddiep.bigdata.data.video.VideoType;
import me.eddiep.jconfig.JConfig;
import org.bson.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static me.eddiep.bigdata.util.RandomHelper.*;

public class VideoStore {
    private static MongoClient client;
    private static MongoDatabase database;
    private static MongoCollection<Document> videoCollection;

    public static void init() {
        File dir = new File("sql");
        if (!dir.exists()) {
            if (!dir.mkdir())
                throw new RuntimeException("Could not create SQL directory!");
        }

        File config = new File(dir, "mongo.conf");
        MongoConfig mongoConfig = JConfig.newConfigObject(MongoConfig.class);
        if (!config.exists()) {
            mongoConfig.save(config);
        } else {
            mongoConfig.load(config);
        }

        MongoClient client = new MongoClient(mongoConfig.getIp(), mongoConfig.getPort());
        database = client.getDatabase(mongoConfig.getDatabaseName());
        if (!doesCollectionExist("videos")) {
            database.createCollection("videos");
            videoCollection = database.getCollection("videos");
            videoCollection.createIndex(new Document().append("url", "text"));
        } else {
            videoCollection = database.getCollection("videos");
        }
    }

    private static boolean doesCollectionExist(String name) {
        for (String collection : database.listCollectionNames()) {
            if (name.equals(collection))
                return true;
        }
        return false;
    }

    public static void saveVideo(Video video) {
        Document document = video.asDocument();

        videoCollection.insertOne(document);
    }

    public static Video getRandomVideo() {
        return Video.toVideo(
                videoCollection.aggregate(
                        Collections.singletonList(
                                Aggregates.sample(1)
                        )
                ).first()
        );
    }

    public static Video getRandomVideo(VideoType type) {
        return Video.toVideo(
                videoCollection.aggregate(
                        Arrays.asList(
                                Aggregates.sample(1),
                                Aggregates.match(Filters.eq("type", type.getType()))
                        )
                ).first()
        );
    }

    public static Video[] getRandomVideos(int count) {
        Video[] videos = new Video[count];
        HashSet<String> urls = new HashSet<String>();

        MongoCursor<Document> documents = videoCollection.aggregate(
                Collections.singletonList(
                        Aggregates.sample(count)
                )
        ).iterator();

        for (int i = 0; i < videos.length; i++) {
            if (!documents.hasNext())
                break;
            Video video = Video.toVideo(documents.next());
            if (urls.contains(video.getUrl())) {
                video = getRandomVideo();
            }

            videos[i] = video;
            urls.add(video.getUrl());
        }

        return videos;
    }

    public static Video[] getRandomVideos(int count, VideoType type) {
        Video[] videos = new Video[count];
        HashSet<String> urls = new HashSet<String>();

        MongoCursor<Document> documents = videoCollection.aggregate(
                Arrays.asList(
                        Aggregates.sample(count),
                        Aggregates.match(Filters.eq("type", type.getType()))
                )
        ).iterator();

        for (int i = 0; i < videos.length; i++) {
            if (!documents.hasNext())
                break;
            Video video = Video.toVideo(documents.next());
            if (urls.contains(video.getUrl())) {
                video = getRandomVideo(type);
            }

            videos[i] = video;
            urls.add(video.getUrl());
        }

        return videos;
    }
}
