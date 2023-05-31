package com.udescbittorrent.trackerHandler;

import com.udescbittorrent.models.PeerDto;
import com.udescbittorrent.models.TrackerDto;

import java.util.*;

public class Tracker {
    private static Tracker instance;
    private static TrackerDto trackerDto;
    private static final List<String> filesChunk = Arrays.asList("1-file.txt","2-file.txt","3-file.txt");
    private Tracker(){
        trackerDto = new TrackerDto(filesChunk);
        System.out.print("Inicializando o tracker com os pedaços de arquivos: " + String.join(", ", filesChunk));
    }
    public static TrackerDto getTrackerDto() {
        return trackerDto;
    }
    public static TrackerDto get(){
        if (instance == null){
            instance = new Tracker();
        }
        return getTrackerDto();
    }
}
