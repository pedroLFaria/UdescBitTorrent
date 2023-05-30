package com.udescbittorrent.peerHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udescbittorrent.ObjectMapperSingleton;
import com.udescbittorrent.models.TrackerDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PeerClient {
    static String trackerUrl = System.getProperty("tracker.url") != null ? System.getProperty("tracker.url") : "http://localhost:8000/";
    static String fileFolders = System.getProperty("peer.files.folder") != null ? System.getProperty("peer.files.folder") : "src/main/resources/file";
    static ObjectMapper mapper = ObjectMapperSingleton.getInstance();
    static Peer peerService = Peer.getInstance();
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void sendInfoToTracker() throws IOException {
        List<String> fileChunks = getFileChunks(fileFolders);
        TrackerDto trackerDto = httpPost(trackerUrl, fileChunks);
        peerService.setPeers(trackerDto.getPeerDtoList());
        peerService.setFilesChunksInfo(trackerDto.getFileChunks());
    }

    public static void getPeersFromTracker() throws IOException {
        TrackerDto trackerDto = httpGet(trackerUrl);
        peerService.setPeers(trackerDto.getPeerDtoList());
        peerService.setFilesChunksInfo(trackerDto.getFileChunks());
    }

    static TrackerDto httpPost(String url, List<String> fileChunks) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(mapper.writeValueAsString(fileChunks)));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(httpPost);
        return getTrackerDto(response);
    }

    static TrackerDto httpGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        return getTrackerDto(response);
    }

    private static TrackerDto getTrackerDto(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException("Sem retorno do tracker");
        }
        String resultString = EntityUtils.toString(entity);
        return mapper.readValue(resultString, TrackerDto.class);
    }

    public static TrackerDto sendFiles(String fileName) throws IOException {
        HttpPost httpPost = new HttpPost(trackerUrl);
        List<String> myFiles = getFileChunks("src/main/resources/file");
        httpPost.setEntity(new StringEntity("[ \"1-file.txt\", \"2-file.txt\" ]"));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(httpPost);
        return getTrackerDto(response);

    }

    public static List<String> getFileChunks(String folderPath) {
        List<String> fileList = new ArrayList<>();

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
}
