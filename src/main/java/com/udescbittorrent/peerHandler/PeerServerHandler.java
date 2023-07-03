package com.udescbittorrent.peerHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.PropertiesService;
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
        if ("POST".equals(request.getRequestMethod())) {
            handlePost(request);
        } else if ("GET".equals(request.getRequestMethod())) {
            handleGet(request);
        }
    }

    private void handleGet(HttpExchange request) throws IOException {
        String urlInfo = Utils.getPathInfo(request, 1);
        if (urlInfo == null) {
            handleResponse(request, HttpStatus.SC_BAD_REQUEST, "Parametro não enviado");
            return;
        }
        String filePath = PropertiesService.peerFileFolders + "/" + urlInfo;
        Path file = Paths.get(filePath);

        if (!Files.exists(file) || !Files.isReadable(file)) {
            handleResponse(request, 404, "File not found\n");
            return;
        }

        try {
            request.getResponseHeaders().set("Content-Type", "application/octet-stream");
            request.sendResponseHeaders(200, Files.size(file));
            System.out.println("Enviando arquivo " + urlInfo + "para o peer " + request.getRemoteAddress().getAddress().getHostAddress());
            OutputStream os = request.getResponseBody();
            Files.copy(file, os);
            os.close();
        } catch (IOException e) {
            handleResponse(request, 500, "Internal server error");
        }
    }

    private void handlePost(HttpExchange request) throws IOException {
        handleResponse(request, HttpStatus.SC_OK, "Hello World!");
    }

    private static void handleResponse(HttpExchange request, int httpStatus, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        request.sendResponseHeaders(httpStatus, response.length());
        request.getResponseBody().write(response.getBytes());
        request.close();
    }
}
