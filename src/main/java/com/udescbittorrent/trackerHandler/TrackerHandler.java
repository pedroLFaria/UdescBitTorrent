package com.udescbittorrent.trackerHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udescbittorrent.ObjectMapperSingleton;
import com.udescbittorrent.models.PeerDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;

public class TrackerHandler implements HttpHandler {
    Tracker tracker = Tracker.get();
    ObjectMapper mapper = ObjectMapperSingleton.getInstance();
    @Override
    public void handle(HttpExchange request) throws IOException {
        if ("POST".equals(request.getRequestMethod())) {
            handlePost(request);
        } else if ("GET".equals(request.getRequestMethod())) {
            handleGet(request);
        }
    }

    private void handleGet(HttpExchange request) throws IOException {
        String responseBody = mapper.writeValueAsString(tracker.getPeerInfoList());
        handleResponse(request, responseBody);
    }

    private void handlePost(HttpExchange request) throws IOException {
        InputStream is = request.getRequestBody();
        String requestBodyJson = readAllBytes(is);
        String[] filesChunk = mapper.readValue(requestBodyJson, String[].class);
        PeerDto newPeer = new PeerDto();
        String peerIpAdress = request.getRemoteAddress().getAddress().getHostAddress();
        newPeer.setIp(peerIpAdress);
        newPeer.setFileChunk(new HashSet<>(Arrays.asList(filesChunk)));
        tracker.getPeerInfoList().add(newPeer);

        String responseBody = mapper.writeValueAsString(tracker.getPeerInfoList());
        handleResponse(request, responseBody);
    }

    private static void handleResponse(HttpExchange request, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        request.sendResponseHeaders(200, response.length());
        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }


}
