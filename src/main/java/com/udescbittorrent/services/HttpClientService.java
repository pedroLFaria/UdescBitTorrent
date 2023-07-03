package com.udescbittorrent.services;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientService {
    private static HttpClient httpClient;

    private HttpClientService() {}

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }
}
