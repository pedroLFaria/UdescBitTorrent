package com.udescbittorrent.services;

import com.sun.net.httpserver.HttpExchange;
import com.udescbittorrent.dtos.PeerDto;
import com.udescbittorrent.dtos.TrackerDto;
import lombok.Getter;

import java.util.AbstractMap;

public class TrackerService {
    private static TrackerService instance;
    @Getter
    private static TrackerDto trackerDto;
    private TrackerService(){
        trackerDto = new TrackerDto();
        System.out.println("Inicializando o tracker");
    }

    public static TrackerDto get(){
        if (instance == null){
            instance = new TrackerService();
        }
        return getTrackerDto();
    }

    public static PeerDto findPeerFromRequest(HttpExchange request) {
        String peerAddress = request.getRemoteAddress().getAddress().getHostAddress();
        return trackerDto.getPeerTable().entrySet().stream()
            .filter(peer -> peer.getValue().getAddress().equals(peerAddress))
            .findFirst().orElse(new AbstractMap.SimpleEntry<>(null, null)).getValue();
    }
}
