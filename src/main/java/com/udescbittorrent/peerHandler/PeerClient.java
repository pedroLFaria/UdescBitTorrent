package com.udescbittorrent.peerHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udescbittorrent.ObjectMapperSingleton;
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
    static String trackerUrl = System.getProperty("tracker.url");
    static String fileFolders = System.getProperty("peer.files.folder");
    static ObjectMapper mapper = ObjectMapperSingleton.getInstance();
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    public static void sendInfoToTracker() throws IOException {
        List<String> fileChunks = getFileChunks(fileFolders);
        httpPost(trackerUrl, fileChunks);
    }

    public static void getPeersFromTracker() {

    }
    static void httpPost(String url, List<String> fileChunks) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(mapper.writeValueAsString(fileChunks)));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(httpPost);
        response.getEntity();
    }


    void httpGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }
    }
    public static void sendFiles() throws IOException {
        HttpPost httpPost = new HttpPost(trackerUrl);
        List<String> myFiles = getFileChunks("src/main/resources/file");

        httpPost.setEntity(new StringEntity("[ \"1-file.txt\", \"2-file.txt\" ]"));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }
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
