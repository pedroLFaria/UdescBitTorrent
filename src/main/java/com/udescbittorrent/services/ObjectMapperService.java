package com.udescbittorrent.services;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperService {
    private static ObjectMapper objectMapper;

    private ObjectMapperService() {}
    public static synchronized ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}
