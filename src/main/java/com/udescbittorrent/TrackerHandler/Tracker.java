package com.udescbittorrent.TrackerHandler;

import java.util.*;

public class Tracker {
    private List<PeerInfo> peerInfoList;
    private static Tracker instance;
    private Tracker(){
        this.peerInfoList = new ArrayList<>();
    }

    public static Tracker get(){
        if (instance == null){
            instance = new Tracker();
        }
        return instance;
    }

    public List<PeerInfo> getPeerInfoList() {
        return peerInfoList;
    }

    public void setPeerInfoList(List<PeerInfo> peerInfoList) {
        this.peerInfoList = peerInfoList;
    }
}
