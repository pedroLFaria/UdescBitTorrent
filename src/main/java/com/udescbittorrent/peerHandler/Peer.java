package com.udescbittorrent.peerHandler;

import java.util.List;

public class Peer {
    private static Peer intance;
    private List<String> filesChunksInfo;

    private Peer() {
    }

    public static Peer getInstance() {
        if (intance == null) {
            intance = new Peer();
        }
        return intance;
    }

    public List<String> getFilesChunksInfo() {
        return filesChunksInfo;
    }

    public void setFilesChunksInfo(List<String> filesChunksInfo) {
        this.filesChunksInfo = filesChunksInfo;
    }
}
