package com.udescbittorrent.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.Utils;
import com.udescbittorrent.services.PeerClientService;
import org.apache.http.HttpStatus;

import java.io.IOException;

public class PeerClientHandler {

    PeerClientService peerClient;

    public PeerClientHandler() {
        peerClient = PeerClientService.get();
    }

    private static void handleResponse(HttpExchange request, int httpStatus, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        request.sendResponseHeaders(httpStatus, response.length());
        request.getResponseBody().write(response.getBytes());
        request.getResponseBody().close();
    }

    // public void handle(String request) throws IOException {
    //     String urlInfo = Utils.getPathInfo(request, 2);
    //     sendMessage(request);        
    //     sendFile(request);
    // }

    // private void signin(String username) throws IOException {
        
    // }

    // private void sendMessage(HttpExchange request) throws IOException {
    //     boolean success = peerClient.getAllMissingChunks();
    //     handleResponse(request, success ? HttpStatus.SC_OK : HttpStatus.SC_INTERNAL_SERVER_ERROR, success ? "Sucesso ao obter arquivo!" : "Falha ao obter arquivo!");
    // }

    // private void sendFile(HttpExchange request) throws IOException {
    //     boolean success = peerClient.getNextMissingChunk();
    //     handleResponse(request, success ? HttpStatus.SC_OK : HttpStatus.SC_INTERNAL_SERVER_ERROR, success ? "Sucesso ao obter arquivo!" : "Falha ao obter arquivo!");
    // }

    private void handlePost(HttpExchange request) throws IOException {
        handleResponse(request, HttpStatus.SC_OK, "Hello World!");
    }
}
