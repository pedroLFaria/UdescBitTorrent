package com.udescbittorrent;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public final class App {
    private App() {
    }
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        // Start server
        new Thread(() -> {
            Peer peer = new Peer();
            peer.startServer(6666);
        }).start();

        // Start client
        new Thread(() -> {
            Peer peer = new Peer();
            peer.startClient("127.0.0.1", 6666);
        }).start();
    }
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Hello, world!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
