package com.udescbittorrent.services;

import com.udescbittorrent.dtos.TrackerDto;

import java.util.*;
import java.util.stream.Collectors;

public class TrackerService {
    private static TrackerService instance;
    private static TrackerDto trackerDto;
    private TrackerService(){
        List<String> fileChunks = Arrays.stream(PropertiesService.fileChunks.split(",")).collect(Collectors.toList());
        trackerDto = new TrackerDto(fileChunks);
        System.out.println("Inicializando o tracker com os pedaços de arquivos: " + String.join(", ", fileChunks));
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
