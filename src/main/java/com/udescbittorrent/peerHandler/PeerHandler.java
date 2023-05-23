package com.udescbittorrent.peerHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;

public class PeerHandler implements HttpHandler {
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
        }
        handleResponse(request, HttpStatus.SC_OK, "Hello World!");
    }

    private static String getPathInfo(HttpExchange request) {
        String path = request.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length > 2) {
            return pathParts[2];
        }
        return null;
    }

    private void handlePost(HttpExchange request) throws IOException {
        handleResponse(request, HttpStatus.SC_OK,"Hello World!");
    }

    private static void handleResponse(HttpExchange request, int httpStatus, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        request.sendResponseHeaders(httpStatus, response.length());
        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
