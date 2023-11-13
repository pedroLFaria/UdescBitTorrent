package com.udescbittorrent.services;

import com.udescbittorrent.Utils;
import com.udescbittorrent.dtos.PeerDto;
import com.udescbittorrent.dtos.TrackerDto;
import lombok.Getter;
import org.apache.http.HttpResponse;

import java.io.File;

@Getter
public class PeerClientService {
    private static PeerClientService instance;
    private TrackerDto tracker;
    private String port;

    public static PeerClientService get() {
        if (instance == null) {
            instance = new PeerClientService("8002");
        }
        return instance;
    }
    public static PeerClientService get(String port) {
        if (instance == null) {
            instance = new PeerClientService(port);
        }
        return instance;
    }
    private PeerClientService(String port) {
        this.port = port;
    }

    public static void sendMessage(String message, String usernameDestination) {
        try {
            HttpResponse response = Utils.httpPost(PropertiesService.trackerAddress+"/command/message/" + usernameDestination,
                message);
            instance.tracker = Utils.mapperToDto(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendFile(String fileName, String usernameDestination) {
        try {
            File file = new File(fileName);
            HttpResponse response = Utils.httpPost(String.format("%s/command/file/%s/%s",
                    PropertiesService.trackerAddress, usernameDestination, file.getName()),
                file);
            instance.tracker = Utils.mapperToDto(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendInfoToTracker(String username) {
        try {
            PeerDto peerDto = new PeerDto(instance.getPort(), username, null);
            HttpResponse response = Utils.httpPost(PropertiesService.trackerAddress+"/user/", peerDto);
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

    public void setTracker(TrackerDto tracker) {
        this.tracker = tracker;
    }

}
