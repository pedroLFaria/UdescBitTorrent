package com.udescbittorrent.TrackerHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udescbittorrent.ObjectMapperSingleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TrackerHandler implements HttpHandler {
    Tracker tracker = Tracker.get();
    ObjectMapper mapper = ObjectMapperSingleton.getInstance();
    @Override
    public void handle(HttpExchange request) throws IOException {
        if ("POST".equals(request.getRequestMethod())) {
            handlePost(request);
        }
    }

    private void handlePost(HttpExchange request) throws IOException {
        InputStream is = request.getRequestBody();
        String requestBodyJson = readAllBytes(is);
        String[] filesChunk = mapper.readValue(requestBodyJson, String[].class);
        PeerInfo newPeer = new PeerInfo();
        String peerIpAdress = request.getRemoteAddress().getAddress().getHostAddress();
        newPeer.setIp(peerIpAdress);
        newPeer.setFileChunk(new HashSet<>(Arrays.asList(filesChunk)));
        tracker.getPeerInfoList().add(newPeer);
        String response = mapper.writeValueAsString(tracker.getPeerInfoList());
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
