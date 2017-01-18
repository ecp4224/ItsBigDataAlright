package me.eddiep.bigdata;

import me.eddiep.bigdata.data.VideoStore;
import me.eddiep.bigdata.data.fetch.FetchTask;
import me.eddiep.bigdata.data.fetch.impl.FacebookFetcher;
import me.eddiep.bigdata.http.HttpListener;
import me.eddiep.bigdata.util.Constants;
import me.eddiep.tinyhttp.TinyHttpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        VideoStore.init(); //Init the database

        FetchTask task = new FetchTask(); //Init the task that fetches videos
        addFetchers(task); //Add all the places to look for videos
        task.start(); //Start the task

        HttpListener listener = new HttpListener(); //Init the HTTP listener
        TinyHttpServer server = new TinyHttpServer(8080, listener, false); //Init the HTTP server

        server.start(); //Start the server
    }

    private static void addFetchers(FetchTask task) {
        task.addFetcher(new FacebookFetcher("" + Constants.GROUP_ID));
    }
}
