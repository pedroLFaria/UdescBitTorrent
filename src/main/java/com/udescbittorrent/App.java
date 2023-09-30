package com.udescbittorrent;

import com.sun.net.httpserver.HttpServer;
import com.udescbittorrent.services.PeerClientService;
import com.udescbittorrent.handlers.PeerServerHandler;
import com.udescbittorrent.handlers.TrackerCommandHandler;
import com.udescbittorrent.services.PropertiesService;
import com.udescbittorrent.handlers.TrackerUserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public final class App {

    static public void main(String[] args) throws IOException {
        System.out.println(PropertiesService.toStrings());
        if ("tracker".equals(PropertiesService.profileName)) {
            HttpServer serverTracker = HttpServer.create(new InetSocketAddress(8000), 0);
            serverTracker.createContext("/user/", new TrackerUserHandler());
            serverTracker.createContext("/command/", new TrackerCommandHandler());
            serverTracker.setExecutor(null);
            serverTracker.start();
        } else {
            int port = Utils.getNextAvailablePort();
            HttpServer serverPeer = HttpServer.create(new InetSocketAddress(port), 0);
            serverPeer.createContext("/server/", new PeerServerHandler());
            serverPeer.setExecutor(null);
            serverPeer.start();            
            PeerClientService.sendInfoToTracker("teste");            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> PeerClientService.unsubscribeFromTracker()));
        }
    }
    

}
