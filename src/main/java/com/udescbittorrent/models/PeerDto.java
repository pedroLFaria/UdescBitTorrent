package com.udescbittorrent.models;

import java.util.HashSet;

public class PeerDto {

    private String ip;
    private HashSet<String> fileChunk;

    public PeerDto(String ip, HashSet<String> fileChunk) {
        this.ip = ip;
        this.fileChunk = fileChunk;
    }
    public PeerDto() {
        this.fileChunk = new HashSet<>();
    }
    public PeerDto(String ip) {
        this.ip = ip;
        this.fileChunk = new HashSet<>();
    }
    public HashSet<String> getFileChunk() {
        return fileChunk;
    }
    public String getIp() {
        return ip;
    }
    public void setFileChunk(HashSet<String> fileChunk) {
        this.fileChunk = fileChunk;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
}
