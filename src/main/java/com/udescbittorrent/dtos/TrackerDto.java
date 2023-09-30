package com.udescbittorrent.dtos;

import java.util.Hashtable;

public class TrackerDto {
    private final Hashtable<String, PeerDto> peerTable;

    public TrackerDto(){
        peerTable = new Hashtable<>();
    }

    public void addPeer(String peerIp, PeerDto newPeer) {
        peerTable.put(peerIp, newPeer);
    }

    public Hashtable<String, PeerDto> getPeerTable() {
        return peerTable;
    }

    @Override
    public TrackerDto clone() {
        TrackerDto tracker = null;
        try {
            tracker = (TrackerDto) super.clone();
        } catch (CloneNotSupportedException e) {
            tracker = new TrackerDto();
        }
        tracker.peerTable.putAll(this.peerTable);
        return tracker;
    }

    @Override
    public String toString() {
        return String.format("TrackerDto{peerTable:{\n%s\n}", peerTable.toString()) ;
    }
}
