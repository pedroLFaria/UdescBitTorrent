package com.udescbittorrent;
import com.sun.net.httpserver.*;
import com.udescbittorrent.peerHandler.PeerClient;
import com.udescbittorrent.peerHandler.PeerServerHandler;
import com.udescbittorrent.trackerHandler.TrackerHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;


public final class App {

    public static void main(String[] args) throws IOException {
        if("tracker".equals(PropertiesService.profileName)) {
            HttpServer serverTracker = HttpServer.create(new InetSocketAddress(8000), 0);
            serverTracker.createContext("/", new TrackerHandler());
            serverTracker.setExecutor(null); // creates a default executor
            serverTracker.start();
        }    else {
            HttpServer serverPeer = HttpServer.create(new InetSocketAddress(8002), 0);
            serverPeer.createContext("/", new PeerServerHandler());
            serverPeer.setExecutor(null); // creates a default executor
            serverPeer.start();
            PeerClient.sendInfoToTracker();
        }
    }

    static void httpPost() throws IOException {
        HttpPost httpPost = new HttpPost("http://localhost:8000/");
        httpPost.setEntity(new StringEntity("[ \"1-file.txt\", \"2-file.txt\" ]"));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }
    }

    void httpGet() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8000/");
        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }
    }
}
