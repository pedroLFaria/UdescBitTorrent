package com.udescbittorrent.TrackerHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PeerInfo {

    private String ip;
    private HashSet<String> fileChunk;

    public PeerInfo(String ip, HashSet<String> fileChunk) {
        this.ip = ip;
        this.fileChunk = fileChunk;
    }
    public PeerInfo() {
        this.fileChunk = new HashSet<>();
    }
    public PeerInfo(String ip) {
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
