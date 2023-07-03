package com.udescbittorrent.trackerHandler;

import com.udescbittorrent.PropertiesService;
import com.udescbittorrent.models.TrackerDto;

import java.util.*;
import java.util.stream.Collectors;

public class Tracker {
    private static Tracker instance;
    private static TrackerDto trackerDto;
    private Tracker(){
        List<String> fileChunks = Arrays.stream(PropertiesService.fileChunks.split(",")).collect(Collectors.toList());
        trackerDto = new TrackerDto(fileChunks);
        System.out.println("Inicializando o tracker com os pedaços de arquivos: " + String.join(", ", fileChunks));
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
