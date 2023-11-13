package com.udescbittorrent.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvValidationException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.dtos.LogDto;
import com.udescbittorrent.dtos.MessageTypes;
import com.udescbittorrent.dtos.PeerDto;
import com.udescbittorrent.services.CsvDatabaseService;
import com.udescbittorrent.services.ObjectMapperService;
import com.udescbittorrent.Utils;
import com.udescbittorrent.dtos.TrackerDto;
import com.udescbittorrent.services.TrackerService;
import lombok.var;
import org.apache.http.entity.InputStreamEntity;

import java.io.*;
import java.time.LocalDateTime;

import static com.udescbittorrent.Utils.getMessageBody;
import static com.udescbittorrent.Utils.httpPost;

public class TrackerCommandHandler implements HttpHandler {

    TrackerDto tracker = TrackerService.get();
    ObjectMapper mapper = ObjectMapperService.getInstance();
    CsvDatabaseService databaseService = CsvDatabaseService.get();

    @Override
    public void handle(HttpExchange request) throws IOException {
        String command = Utils.getPathInfo(request, 2);
        String userName = Utils.getPathInfo(request, 3);
        PeerDto senderPeer = TrackerService.findPeerFromRequest(request);
        PeerDto recipientPeer = tracker.getPeerTable().get(userName);
        if(senderPeer == null || recipientPeer == null){
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
        if ("message".equalsIgnoreCase(command)) {
            handleMessage(request, senderPeer, recipientPeer);
        } else if ("file".equalsIgnoreCase(command)) {
            String fileName = Utils.getPathInfo(request, 4);
            handleFile(request, senderPeer, recipientPeer, fileName);
        }
    }

    private void handleMessage(HttpExchange request, PeerDto senderPeer, PeerDto recipientPeer) throws IOException {
        String message = getMessageBody(request);
        String url = getUrl(senderPeer, recipientPeer, "message");
        Utils.httpPost(url, message);
        logRequest(senderPeer, recipientPeer, MessageTypes.MESSAGE, message);
        handleResponseToPeer(request, senderPeer);
    }

    private void logRequest(PeerDto senderPeer, PeerDto recipientPeer, MessageTypes messageType, String contend) throws IOException {
        LogDto row = new LogDto(senderPeer.getAddress(), senderPeer.getUserName(),
            recipientPeer.getAddress(), recipientPeer.getUserName(), messageType.toString(), contend,
            LocalDateTime.now().toString());
        try {
            databaseService.addRow(row);
        } catch (Exception e) {
            System.out.printf("Failed to add row to DataBase: %s\n", row);
        }
    }

    private String getUrl(PeerDto senderDto, PeerDto peerToSend, String method) {
        var senderUsername = senderDto.getUserName();
        return String.format("http://%s:%s/%s/%s", peerToSend.getAddress(), peerToSend.getPort(), method, senderUsername);
    }

    private void handleFile(HttpExchange request, PeerDto senderPeer, PeerDto recipientPeer, String fileName) throws IOException {
        InputStream is = request.getRequestBody();
        var entity = new InputStreamEntity(is);
        String url = getUrl(senderPeer, recipientPeer, "file").concat("/" + fileName);
        httpPost(url, entity);
        is.close();
        handleResponseToPeer(request, senderPeer);
        logRequest(senderPeer, recipientPeer, MessageTypes.FILE, fileName);
    }

    private void handleResponseToPeer(HttpExchange request, PeerDto senderPeer) throws IOException {
        TrackerDto response = tracker.clone();
        response.getPeerTable().remove(senderPeer.getUserName());
        String responseBody = mapper.writeValueAsString(response);
        System.out.println("Respondendo para peer: " + senderPeer);
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
}
