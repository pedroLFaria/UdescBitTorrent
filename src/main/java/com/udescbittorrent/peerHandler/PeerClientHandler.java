package com.udescbittorrent.peerHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.Utils;
import org.apache.http.HttpStatus;

import java.io.IOException;

public class PeerClientHandler implements HttpHandler {

    PeerClient peerClient;

    public PeerClientHandler() {
        peerClient = PeerClient.get();
    }

    private static void handleResponse(HttpExchange request, int httpStatus, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        request.sendResponseHeaders(httpStatus, response.length());
    }

    @Override
    public void handle(HttpExchange request) throws IOException {
        String urlInfo = Utils.getPathInfo(request, 2);
        if ("all".equals(urlInfo)) {
            handleGetAll(request);
        }
        handleGetNext(request);
    }

    private void handleGetAll(HttpExchange request) throws IOException {
        boolean success = peerClient.getAllMissingChunks();
        handleResponse(request, success ? HttpStatus.SC_OK : HttpStatus.SC_INTERNAL_SERVER_ERROR, success ? "Sucesso ao obter arquivo!" : "Falha ao obter arquivo!");
    }

    private void handleGetNext(HttpExchange request) throws IOException {
        boolean success = peerClient.getNextMissingChunk();
        handleResponse(request, success ? HttpStatus.SC_OK : HttpStatus.SC_INTERNAL_SERVER_ERROR, success ? "Sucesso ao obter arquivo!" : "Falha ao obter arquivo!");
    }

    private void handlePost(HttpExchange request) throws IOException {
        handleResponse(request, HttpStatus.SC_OK, "Hello World!");
    }
}
