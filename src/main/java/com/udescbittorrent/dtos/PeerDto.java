package com.udescbittorrent.dtos;

import java.util.HashSet;

public class PeerDto {
    private String port;
    private HashSet<String> fileChunk;

    public PeerDto(String port, HashSet<String> fileChunk) {
        this.port = port;
        this.fileChunk = fileChunk;
    }

    public PeerDto() {
        this.fileChunk = new HashSet<>();
    }

    public HashSet<String> getFileChunk() {
        return fileChunk;
    }

    public void setFileChunk(HashSet<String> fileChunk) {
        this.fileChunk = fileChunk;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
