package com.udescbittorrent;

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
