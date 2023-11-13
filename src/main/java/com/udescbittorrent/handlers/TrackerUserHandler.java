package com.udescbittorrent.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.Utils;
import com.udescbittorrent.services.ObjectMapperService;
import com.udescbittorrent.dtos.PeerDto;
import com.udescbittorrent.dtos.TrackerDto;
import com.udescbittorrent.services.TrackerService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TrackerUserHandler implements HttpHandler {

    TrackerDto tracker = TrackerService.get();
    ObjectMapper mapper = ObjectMapperService.getInstance();

    @Override
    public void handle(HttpExchange request) throws IOException {
        PeerDto peer = TrackerService.findPeerFromRequest(request);
        if ("POST".equals(request.getRequestMethod())) {
            handlePost(request);
        } else if ("GET".equals(request.getRequestMethod())) {
            handleGet(request, peer);
        } else if ("DELETE".equals(request.getRequestMethod())) {
            handleDelete(request, peer);
        }
    }

    private void handleGet(HttpExchange request, PeerDto peer) throws IOException {
        handleResponseToPeer(request, peer);
    }

    private void handlePost(HttpExchange request) throws IOException {
        PeerDto newPeer = getPeerDto(request);
        tracker.addPeer(newPeer.getUserName(), newPeer);
        System.out.println("Novo peer adicionado a rede: " + newPeer);
        handleResponseToPeer(request, newPeer);
    }

    private void handleDelete(HttpExchange request, PeerDto peer) throws IOException {
        tracker.getPeerTable().remove(peer.getUserName());
        System.out.println("Removendo o peer " + peer);
        handleResponse(request, "Peer removido " + peer);
    }

    private void handleResponseToPeer(HttpExchange request, PeerDto peer) throws IOException {
        TrackerDto response = tracker.clone();
        response.getPeerTable().remove(peer.getUserName());
        String responseBody = mapper.writeValueAsString(response);
        System.out.println("Respondendo para peer: " + peer);
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

    private PeerDto getPeerDto(HttpExchange request) throws IOException {
        InputStream is = request.getRequestBody();
        String requestBodyJson = Utils.readAllBytes(is);
        String peerAddress = request.getRemoteAddress().getAddress().getHostAddress();
        PeerDto peer = mapper.readValue(requestBodyJson, PeerDto.class);
        peer.setAddress(peerAddress);
        return peer;
    }
}
