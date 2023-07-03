package com.udescbittorrent;

import com.sun.net.httpserver.HttpServer;
import com.udescbittorrent.services.PeerClientService;
import com.udescbittorrent.handlers.PeerClientHandler;
import com.udescbittorrent.handlers.PeerServerHandler;
import com.udescbittorrent.services.PropertiesService;
import com.udescbittorrent.handlers.TrackerHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public final class App {

    static public void main(String[] args) throws IOException {
        System.out.println(PropertiesService.toStrings());
        if ("tracker".equals(PropertiesService.profileName)) {
            HttpServer serverTracker = HttpServer.create(new InetSocketAddress(8000), 0);
            serverTracker.createContext("/", new TrackerHandler());
            serverTracker.setExecutor(null);
            serverTracker.start();
        } else {
            int port = getNextAvailablePort();
            PeerClientService peerClient = PeerClientService.get(String.valueOf(port));
            HttpServer serverPeer = HttpServer.create(new InetSocketAddress(port), 0);
            serverPeer.createContext("/server/", new PeerServerHandler());
            serverPeer.createContext("/client/", new PeerClientHandler());
            serverPeer.setExecutor(null);
            serverPeer.start();
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            peerClient.sendInfoToTracker(executor, Long.parseLong(PropertiesService.peerThreadSleepTime));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> PeerClientService.unsubscribeFromTracker(executor)));
        }
    }
    public static int getNextAvailablePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            System.out.println("Falha ao obter uma porta!");
            return -1;
        }
    }

}
