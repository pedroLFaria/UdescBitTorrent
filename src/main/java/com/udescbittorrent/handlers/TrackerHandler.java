package com.udescbittorrent.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.services.ObjectMapperService;
import com.udescbittorrent.dtos.PeerDto;
import com.udescbittorrent.dtos.TrackerDto;
import com.udescbittorrent.services.TrackerService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;

public class TrackerHandler implements HttpHandler {
    TrackerDto tracker = TrackerService.get();
    ObjectMapper mapper = ObjectMapperService.getInstance();

    private static void handleResponse(HttpExchange request, String response) throws IOException {
        request.getResponseHeaders().set("Content-Type", "application/json");
        if(response == null){
            request.sendResponseHeaders(204, 0);
            request.getResponseBody().close();
        }
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

    @Override
    public void handle(HttpExchange request) throws IOException {
        if ("POST".equals(request.getRequestMethod())) {
            handlePost(request);
        } else if ("GET".equals(request.getRequestMethod())) {
            handleGet(request);
        } else if ("DELETE".equals(request.getRequestMethod())) {
            handleDelete(request);
        }
    }

    private void handleDelete(HttpExchange request) throws IOException {
        String peerAddress = request.getRemoteAddress().getAddress().getHostAddress();
        tracker.getPeerTable().remove(peerAddress);
        System.out.println("Removendo o peer " + peerAddress);
        handleResponse(request, null);
    }

    private void handleGet(HttpExchange request) throws IOException {
        TrackerDto response = tracker.clone();
        String peerAddress = request.getRemoteAddress().getAddress().getHostAddress();
        response.getPeerTable().remove(peerAddress);
        String responseBody = mapper.writeValueAsString(response);

        System.out.println("Respondendo para peer: " + peerAddress);

        handleResponse(request, responseBody);
    }

    private void handlePost(HttpExchange request) throws IOException {
        String peerIp = request.getRemoteAddress().getAddress().getHostAddress();
        PeerDto newPeer = getPeerDto(request);
        tracker.addPeer(peerIp, newPeer);

        System.out.println("Novo peer adicionado a rede: " + peerIp);

        TrackerDto response = tracker.clone();
        response.getPeerTable().remove(peerIp);
        String responseBody = mapper.writeValueAsString(response);
        handleResponse(request, responseBody);
    }

    private PeerDto getPeerDto(HttpExchange request) throws IOException {
        InputStream is = request.getRequestBody();
        String requestBodyJson = readAllBytes(is);
        return mapper.readValue(requestBodyJson, PeerDto.class);
    }
}
