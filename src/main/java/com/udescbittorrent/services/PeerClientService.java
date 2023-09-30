package com.udescbittorrent.services;

import com.udescbittorrent.Utils;
import com.udescbittorrent.dtos.MessageDto;
import com.udescbittorrent.dtos.TrackerDto;
import org.apache.http.HttpResponse;
import java.util.concurrent.ScheduledExecutorService;

public class PeerClientService {
    private static PeerClientService instance;
    private TrackerDto tracker;

    public static PeerClientService get() {
        if (instance == null) {
            instance = new PeerClientService();
        }
        return instance;
    }

    // public static void sendFile(String message, String filePath) {
    //     HttpRequest request = new HttpPost();
    //     try {            
    //         File file = new File(filePath);
    //         HttpResponse response = Utils.httpPostFile(PropertiesService.trackerAddress, file);
    //         instance.tracker = Utils.mapperToDto(response);
    //     } catch (Exception e) {
    //         System.out.println(e.getMessage());
    //     }
    // }

    public static void sendMessage(String message, String usernameDestination) {
        try {
            MessageDto messageDto = new MessageDto(message, usernameDestination);            
            HttpResponse response = Utils.httpPost(PropertiesService.trackerAddress, messageDto);
            instance.tracker = Utils.mapperToDto(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendInfoToTracker(String username) {
        try {            
            HttpResponse response = Utils.httpPost(PropertiesService.trackerAddress, username);
            instance.tracker = Utils.mapperToDto(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void unsubscribeFromTracker(){
        try {
            Utils.httpDelete(PropertiesService.trackerAddress);
            System.out.println("Peer removido do tracker!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public TrackerDto getTracker() {
        return tracker;
    }

    public void setTracker(TrackerDto tracker) {
        this.tracker = tracker;
    }

}
