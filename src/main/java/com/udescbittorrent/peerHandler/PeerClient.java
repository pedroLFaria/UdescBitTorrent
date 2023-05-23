package com.udescbittorrent.peerHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PeerClient {

    static String trackerUrl = "";
    static void sendFiles() throws IOException {
        HttpPost httpPost = new HttpPost(trackerUrl);
        httpPost.setEntity(new StringEntity("[ \"file1.txt\", \"file2.txt\" ]"));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }
    }
}
