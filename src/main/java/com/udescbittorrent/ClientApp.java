package com.udescbittorrent;

import com.sun.net.httpserver.HttpServer;
import com.udescbittorrent.handlers.PeerServerHandler;
import com.udescbittorrent.services.PeerClientService;
import com.udescbittorrent.services.PeerCommandLineService;
import lombok.var;

import java.io.IOException;
import java.net.InetSocketAddress;

import static com.udescbittorrent.services.PropertiesService.userName;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        int port = Utils.getNextAvailablePort();
        PeerClientService.get(String.valueOf(port));
        System.out.printf("Porta da aplicacao: %d\n", port);
        startPeerServer(port);
        startCommandLine();
        Runtime.getRuntime().addShutdownHook(new Thread(PeerClientService::unsubscribeFromTracker));
    }

    private static void startCommandLine() {
        var commandLineService = new PeerCommandLineService();
        try {
            commandLineService.start();
        } catch (Exception e) {
            System.out.printf("Erro na execução da linha de comando: %s\nPor favor reinicie o serviço\n", e.getMessage());
        }
    }

    private static void startPeerServer(int port) throws IOException {
        HttpServer serverPeer = HttpServer.create(new InetSocketAddress(port), 0);
        serverPeer.createContext("/", new PeerServerHandler());
        serverPeer.setExecutor(null);
        serverPeer.start();
        PeerClientService.sendInfoToTracker(userName);
    }
}
