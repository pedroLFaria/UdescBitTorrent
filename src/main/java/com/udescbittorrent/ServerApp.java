package com.udescbittorrent;

import com.sun.net.httpserver.HttpServer;
import com.udescbittorrent.handlers.TrackerCommandHandler;
import com.udescbittorrent.handlers.TrackerUserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        HttpServer serverTracker = HttpServer.create(new InetSocketAddress(8000), 0);
        serverTracker.createContext("/user/", new TrackerUserHandler());
        serverTracker.createContext("/command/", new TrackerCommandHandler());
        serverTracker.setExecutor(null);
        serverTracker.start();
    }
}
