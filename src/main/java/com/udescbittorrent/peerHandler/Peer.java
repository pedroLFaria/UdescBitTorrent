package com.udescbittorrent.peerHandler;

import com.udescbittorrent.models.PeerDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Peer {
    private List<PeerDto> peerDtoList;
    private Map<String, List<String>> chunkToPeerMap;
    private List<String> filesChunksInfo;
    private static Peer intance;

    private Peer() {
        peerDtoList = new ArrayList<>();
        chunkToPeerMap = new HashMap<>();
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

    public Map<String, List<String>> getChunkToPeerMap() {
        return chunkToPeerMap;
    }
    public void initiateChunkMap(List<String> chunks) {
        chunkToPeerMap = new HashMap<>();
        chunks.forEach( chunk -> chunkToPeerMap.put(chunk, new ArrayList<>()));
    }
    public void addPeerToChunkMap(String chunk, String peerIp){
        this.chunkToPeerMap.get(chunk).add(peerIp);
    }
}
