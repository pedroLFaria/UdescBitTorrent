package com.udescbittorrent;
import com.sun.net.httpserver.*;
/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
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
}
