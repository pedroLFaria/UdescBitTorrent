package com.udescbittorrent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.udescbittorrent.dtos.TrackerDto;
import com.udescbittorrent.services.HttpClientService;
import com.udescbittorrent.services.ObjectMapperService;
import com.udescbittorrent.services.PropertiesService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Utils {
    private static final HttpClient httpClient = HttpClientService.getHttpClient();
    static ObjectMapper mapper = ObjectMapperService.getInstance();

    public static HashSet<String> getFileChunks() {
        HashSet<String> fileList = new HashSet<>();
        String folderPath = PropertiesService.peerFileFolders;
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file.getName());
                    }
                }
            }
        }
        return fileList;
    }
    
    // public static HttpResponse httpPost(String url, File file) {
    //     HttpPost httpPost = new HttpPost(url);
    //     try {
    //         BufferedReader reader = new BufferedReader(new FileReader(file));
    //         file.
    //         CloseableHttpClient httpclient = HttpClients.createDefault();
    //         HttpResponse response = httpclient.execute(httpPost);
    //         System.out.println("Tracker response " + response.getStatusLine());
    //         return response;
    //     } catch (IOException e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    public static HttpResponse httpPost(String url, Object body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        try {
            httpPost.setEntity(new StringEntity(mapper.writeValueAsString(body)));
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(httpPost);
            System.out.println("Tracker response " + response.getStatusLine());
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse httpGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return httpClient.execute(httpGet);
    }

    public static HttpResponse httpDelete(String url) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        return httpClient.execute(httpDelete);
    }
    public static TrackerDto mapperToDto(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            System.out.println("Sem retorno do tracker");
        }
        String resultString;
        TrackerDto trackerDto;
        try {
            resultString = EntityUtils.toString(entity);
            trackerDto = mapper.readValue(resultString, TrackerDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return trackerDto;
    }

    public static String getPathInfo(HttpExchange request, int part) {
        String path = request.getRequestURI().getPath();
        List<String> pathParts = Arrays.asList(path.split("/"));
        String result = null;
        try{
            result = pathParts.get(part);
        } catch (Exception e){
            System.out.println("Falha ao obter informação");
        }
        return result;
    }

    public static int getNextAvailablePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            System.out.println("Falha ao obter uma porta!");
            return -1;
        }
    }
}
