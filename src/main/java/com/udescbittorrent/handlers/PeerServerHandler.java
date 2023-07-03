package com.udescbittorrent.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.services.PropertiesService;
import com.udescbittorrent.Utils;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PeerServerHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange request) throws IOException {
        handleGet(request);
    }

    private void handleGet(HttpExchange request) throws IOException {
        String urlInfo = Utils.getPathInfo(request, 2);
        if (urlInfo == null) {
            handleResponse(request, HttpStatus.SC_BAD_REQUEST, "Parametro não enviado");
            return;
        }
        Path file = Paths.get(PropertiesService.peerFileFolders, urlInfo);

        if (!Files.exists(file) || !Files.isReadable(file)) {
            handleResponse(request, HttpStatus.SC_NOT_FOUND, "File not found\n");
            return;
        }

        try {
            request.getResponseHeaders().set("Content-Type", "application/octet-stream");
            request.sendResponseHeaders(HttpStatus.SC_OK, Files.size(file));
            System.out.println("Enviando arquivo " + urlInfo + "para o peer " + request.getRemoteAddress().getAddress().getHostAddress());
            OutputStream os = request.getResponseBody();
            Files.copy(file, os);
            os.close();
        } catch (IOException e) {
            handleResponse(request, 500, "Internal server error");
        }
    }

    private static void handleResponse(HttpExchange request, int httpStatus, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        request.sendResponseHeaders(httpStatus, response.length());
        request.getResponseBody().write(response.getBytes());
        request.close();
    }
}
