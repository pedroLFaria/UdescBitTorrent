package com.udescbittorrent.services;

import com.udescbittorrent.dtos.TrackerDto;

public class TrackerService {
    private static TrackerService instance;
    private static TrackerDto trackerDto;
    private TrackerService(){        
        trackerDto = new TrackerDto();
        System.out.println("Inicializando o tracker");
    }
    public static TrackerDto getTrackerDto() {
        return trackerDto;
    }
    public static TrackerDto get(){
        if (instance == null){
            instance = new TrackerService();
        }
        return getTrackerDto();
    }
}
