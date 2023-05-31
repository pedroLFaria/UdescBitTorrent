package com.udescbittorrent.peerHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PeerServerHandler implements HttpHandler {
    static String fileFolders = System.getProperty("peer.files.folder") != null ? System.getProperty("peer.files.folder") : "src/main/resources/file";

    @Override
    public void handle(HttpExchange request) throws IOException {
        if ("POST".equals(request.getRequestMethod())) {
            handlePost(request);
        } else if ("GET".equals(request.getRequestMethod())) {
            handleGet(request);
        }
    }

    private void handleGet(HttpExchange request) throws IOException {
        String urlInfo = getPathInfo(request);        
        if (urlInfo == null) {
            handleResponse(request, HttpStatus.SC_BAD_REQUEST, "Parametro não enviado");
            return;
        }
        String filePath = "src/main/resources/file/" + urlInfo;
        Path file = Paths.get(filePath);

        if (!Files.exists(file) || !Files.isReadable(file)) {
            handleResponse(request, 404, "File not found");
            return;
        }

        try {
            request.getResponseHeaders().set("Content-Type", "application/octet-stream");
            request.sendResponseHeaders(200, Files.size(file));
            System.out.print("Enviando arquivo " + urlInfo + "para o peer " + request.getRemoteAddress().getAddress().getHostAddress());
            OutputStream os = request.getResponseBody();
            Files.copy(file, os);
            os.close();
        } catch (IOException e) {
            handleResponse(request, 500, "Internal server error");
        }
    }

    private static String getPathInfo(HttpExchange request) {
        String path = request.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        return pathParts[1];
    }

    private void handlePost(HttpExchange request) throws IOException {
        handleResponse(request, HttpStatus.SC_OK, "Hello World!");
    }

    private static void handleResponse(HttpExchange request, int httpStatus, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        request.sendResponseHeaders(httpStatus, response.length());
    }
}
