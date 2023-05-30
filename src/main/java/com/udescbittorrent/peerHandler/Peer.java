package com.udescbittorrent.peerHandler;

import com.udescbittorrent.models.PeerDto;

import java.util.ArrayList;
import java.util.List;

public class Peer {
    private List<PeerDto> peerDtoList;
    private List<String> filesChunksInfo;
    private static Peer intance;

    private Peer() {
        peerDtoList = new ArrayList<>();
    }

    public static Peer getInstance() {
        if(intance == null) {
            intance = new Peer();
        }
        return intance;
    }

    public void setPeers(List<PeerDto> newPeerDtoList){
        peerDtoList = newPeerDtoList;
    }

    public List<PeerDto> getPeers(){
        return peerDtoList;
    }


    public List<String> getFilesChunksInfo() {
        return filesChunksInfo;
    }

    public void setFilesChunksInfo(List<String> filesChunksInfo) {
        this.filesChunksInfo = filesChunksInfo;
    }
}
