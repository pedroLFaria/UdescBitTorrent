package com.udescbittorrent.trackerHandler;

import com.udescbittorrent.models.PeerDto;

import java.util.*;

public class Tracker {
    private List<PeerDto> peerDtoList;
    private static Tracker instance;
    private Tracker(){
        this.peerDtoList = new ArrayList<>();
    }

    public static Tracker get(){
        if (instance == null){
            instance = new Tracker();
        }
        return instance;
    }

    public List<PeerDto> getPeerInfoList() {
        return peerDtoList;
    }

    public void setPeerInfoList(List<PeerDto> peerDtoList) {
        this.peerDtoList = peerDtoList;
    }
}
