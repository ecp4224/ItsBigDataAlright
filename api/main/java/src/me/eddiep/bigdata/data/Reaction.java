package me.eddiep.bigdata.data;

import org.bson.Document;

public class Reaction {
    private int likes;
    private int angry;
    private int wow;
    private int haha;
    private int love;
    private int sad;

    public Reaction(Document document) {
        likes = document.getInteger("like");
        angry = document.getInteger("angry");
        wow = document.getInteger("wow");
        haha = document.getInteger("haha");
        love = document.getInteger("love");
        sad = document.getInteger("sad");
    }

    public Reaction(int likes, int angry, int wow, int laugh, int love, int sad) {
        this.likes = likes;
        this.angry = angry;
        this.wow = wow;
        this.haha = laugh;
        this.love = love;
        this.sad = sad;
    }

    public int getLikes() {
        return likes;
    }

    public int getAngry() {
        return angry;
    }

    public int getWow() {
        return wow;
    }

    public int getHaha() {
        return haha;
    }

    public int getLove() {
        return love;
    }

    public int getSad() {
        return sad;
    }

    public Document asDocument() {
        return new Document()
                .append("like", likes)
                .append("angry", angry)
                .append("wow", wow)
                .append("haha", haha)
                .append("love", love)
                .append("sad", sad);
    }
}
