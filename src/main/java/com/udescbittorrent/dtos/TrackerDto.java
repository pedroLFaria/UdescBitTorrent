package com.udescbittorrent.dtos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class TrackerDto {
    //192.168.10.15: 1-file, 2-file, 3-file
    private final Hashtable<String, HashSet<String>> peerTable;
    private final List<String> fileChunks;

    public TrackerDto(){
        peerTable = new Hashtable<>();
        fileChunks = new ArrayList<>();
    }
    public TrackerDto(List<String> fileChunks) {
        peerTable = new Hashtable<>();
        this.fileChunks = fileChunks;
    }

    public void addPeer(PeerDto newPeer) {
        peerTable.put(newPeer.getIp(), newPeer.getFileChunk());
    }

    public Hashtable<String, HashSet<String>> getPeerTable() {
        return peerTable;
    }

    public List<String> getFileChunks() {
        return fileChunks;
    }

    @Override
    public TrackerDto clone() {
        TrackerDto tracker = null;
        try {
            tracker = (TrackerDto) super.clone();
        } catch (CloneNotSupportedException e) {
            tracker = new TrackerDto(this.fileChunks);
        }
        tracker.peerTable.putAll(this.peerTable);
        return tracker;
    }

    @Override
    public String toString() {
        return "TrackerDto{" +
            "peerTable: " + peerTable +
            ", fileChunks: [" + String.join(", ", fileChunks) +
            "]}";
    }
}