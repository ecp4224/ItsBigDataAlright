package me.eddiep.bigdata.data.video;

import me.eddiep.bigdata.data.Reaction;
import org.bson.Document;

public class Video {
    private VideoType type;
    private String url;
    private long time_posted;
    private Reaction reaction;

    public static Video toVideo(Document document) {
        Video video = new Video();
        String url = document.getString("url");
        String type = document.getString("type");
        long time_posted = document.getLong("time_posted");

        Document document1 = document.get("reacts", Document.class);


        video.url = url;
        for (VideoType t : VideoType.values()) {
            if (t.getType().equals(type)) {
                video.type = t;
                break;
            }
        }

        video.time_posted = time_posted;
        video.reaction = new Reaction(document1);

        return video;
    }

    public static Video toVideo(String url, VideoType type, Reaction reaction) {
        Video video = new Video();
        video.url = url;
        video.type = type;
        video.reaction = reaction;
        video.time_posted = System.currentTimeMillis();

        return video;
    }

    private Video() { }

    public Reaction getReaction() {
        return reaction;
    }

    public VideoType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public long getTimePosted() {
        return time_posted;
    }

    public Document asDocument() {
        return new Document()
                .append("url", url)
                .append("type", type.getType())
                .append("time_posted", time_posted)
                .append("reacts", reaction.asDocument());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        if (time_posted != video.time_posted) return false;
        if (type != video.type) return false;
        return url.equals(video.url);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (int) (time_posted ^ (time_posted >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Video{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", time_posted=" + time_posted +
                '}';
    }
}
