package com.udescbittorrent.models;

import java.util.ArrayList;
import java.util.List;

public class TrackerDto {
    private List<PeerDto> peerDtoList;
    private List<String> fileChunks;

    public TrackerDto(List<String> fileChunks){
        peerDtoList = new ArrayList<>();
        this.fileChunks = fileChunks;
    }
    public TrackerDto(List<PeerDto> peerDtoList, List<String> fileChunks) {
        this.peerDtoList = peerDtoList;
        this.fileChunks = fileChunks;
    }

    public List<PeerDto> getPeerInfoList() {
        return peerDtoList;
    }

    public void setPeerInfoList(List<PeerDto> peerDtoList) {
        this.peerDtoList = peerDtoList;
    }

    public List<String> getFileChunks() {
        return fileChunks;
    }

    public void setFileChunks(List<String> fileChunks) {
        this.fileChunks = fileChunks;
    }
}
