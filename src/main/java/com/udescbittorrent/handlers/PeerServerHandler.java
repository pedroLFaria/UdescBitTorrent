package com.udescbittorrent.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.udescbittorrent.services.PropertiesService;
import com.udescbittorrent.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PeerServerHandler implements HttpHandler {

    public void handle(HttpExchange request) throws IOException {
        String command = Utils.getPathInfo(request, 1);
        String userName = Utils.getPathInfo(request, 2);
        if ("message".equalsIgnoreCase(command)) {
            handleMessage(request, userName);
        } else if ("file".equalsIgnoreCase(command)) {
            String fileName = Utils.getPathInfo(request, 3);
            handleFile(request, userName, fileName);
        }
    }

    private void handleFile(HttpExchange request, String userName, String fileName) throws IOException {
        handleDirectory(userName);
        if(StringUtils.isEmpty(fileName)) fileName = "file.txt";
        File receivedFile = new File(String.format("files/%s/%s",userName, fileName));
        try (InputStream is = request.getRequestBody();
            FileOutputStream fos = new FileOutputStream(receivedFile)) {
            byte[] buffer = new byte[1024];
            int readBytes;

            while ((readBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readBytes);
            }
            System.out.printf("\nArquivo %s recebido do usuário %s\n", fileName, userName);
            handleResponse(request, HttpStatus.SC_OK, "");
        } catch (Exception e){
            System.out.println("Erro ao armazenar arquivo: " + e.getMessage());
        }
    }

    private void handleDirectory(String userName) {
        File directory = new File(String.format("files/%s/",userName));
        if(!directory.exists()) directory.mkdirs();
    }

    private void handleMessage(HttpExchange request, String userName) throws IOException {
        String message = Utils.getMessageBody(request);
        System.out.printf("\n%s: %s\n", userName, message);
        handleResponse(request, HttpStatus.SC_OK, "");
    }

    private void handlePost(HttpExchange request) throws IOException {
        String urlInfo = Utils.getPathInfo(request, 2);
        if (urlInfo == null || urlInfo.equals("message") || urlInfo.equals("file")) {
            handleResponse(request, HttpStatus.SC_BAD_REQUEST, "Parametro não enviado");
            return;
        }

        try {
            request.getResponseHeaders().set("Content-Type", "application/octet-stream");
            System.out.println("Enviando arquivo " + urlInfo + "para o peer " + request.getRemoteAddress().getAddress().getHostAddress());
            OutputStream os = request.getResponseBody();
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
