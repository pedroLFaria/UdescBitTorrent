package com.udescbittorrent.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.services.ObjectMapperService;
import com.udescbittorrent.Utils;
import com.udescbittorrent.dtos.PeerDto;
import com.udescbittorrent.dtos.TrackerDto;
import com.udescbittorrent.services.TrackerService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TrackerCommandHandler implements HttpHandler {

    TrackerDto tracker = TrackerService.get();
    ObjectMapper mapper = ObjectMapperService.getInstance();

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
        String command = Utils.getPathInfo(request, 2);
        String userName = Utils.getPathInfo(request, 3);
        if ("signin".equalsIgnoreCase(command)) {
            handleSignin(request, userName);
        } else if ("message".equalsIgnoreCase(command)) {
            handleMessage(request, userName);
        } else if ("file".equalsIgnoreCase(command)) {
            handleFile(request, userName);
        }
    }

    private void handleSignin(HttpExchange request, String userName) throws IOException {
        String peerAddress = request.getRemoteAddress().getAddress().getHostAddress();
        String newPeerPort = getPeerDto(request);
        PeerDto newPeer = new PeerDto(newPeerPort, userName);
        tracker.addPeer(peerAddress, newPeer);
        System.out.println("Novo peer adicionado a rede: " + peerAddress);
        handleRespondToPeer(request, peerAddress);
    }

    private void handleMessage(HttpExchange request, String userName) throws IOException {
        String peerAddress = request.getRemoteAddress().getAddress().getHostAddress();
        handleRespondToPeer(request, peerAddress);
    }

    private void handleFile(HttpExchange request, String userName) throws IOException {
        String peerAddress = request.getRemoteAddress().getAddress().getHostAddress();
        tracker.getPeerTable().remove(peerAddress);
        System.out.println("Removendo o peer " + peerAddress);
        handleResponse(request, null);
    }

    private void handleRespondToPeer(HttpExchange request, String peerAddress) throws JsonProcessingException, IOException {
        TrackerDto response = tracker.clone();
        response.getPeerTable().remove(peerAddress);
        String responseBody = mapper.writeValueAsString(response);
        System.out.println("Respondendo para peer: " + peerAddress);
        handleResponse(request, responseBody);
    }

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

    private String getPeerDto(HttpExchange request) throws IOException {
        InputStream is = request.getRequestBody();
        return readAllBytes(is);
    }
}
