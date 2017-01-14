package me.eddiep.bigdata;

import me.eddiep.bigdata.http.HttpListener;
import me.eddiep.tinyhttp.TinyHttpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpListener listener = new HttpListener();
        TinyHttpServer server = new TinyHttpServer(80, listener, false);

        server.start();
    }
}
