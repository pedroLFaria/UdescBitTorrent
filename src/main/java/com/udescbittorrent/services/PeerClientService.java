package com.udescbittorrent.services;

import com.udescbittorrent.Utils;
import com.udescbittorrent.dtos.TrackerDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PeerClientService {
    private static PeerClientService instance;
    private TrackerDto tracker;

    private PeerClientService() {
    }

    public static PeerClientService get() {
        if (instance == null) {
            instance = new PeerClientService();
        }
        return instance;
    }

    public static void sendInfoToTracker() {
        try {
            List<String> fileChunks = Utils.getFileChunks();
            HttpResponse response = Utils.httpPost(PropertiesService.trackerAddress, fileChunks);
            instance.tracker = Utils.mapperToDto(response);
            System.out.println("Sending informations to Tracker (" + PropertiesService.trackerAddress + "): " + String.join(", ", fileChunks));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean getAllMissingChunks() throws IOException {
        List<String> missingChunks;
        while((missingChunks = getMissingChunks()) != null){
            boolean success = getNextRareChunk(missingChunks);
            if(!success)
                return false;
        }
        return true;
    }


    public boolean getNextMissingChunk() throws IOException {
        List<String> missingChunks = getMissingChunks();
        if(missingChunks ==null){
            return true;
        }
        return getNextRareChunk(missingChunks);
    }


    private List<String> getMissingChunks() {
        List<String> myChunks = Utils.getFileChunks();
        List<String> missingChunks = tracker.getFileChunks().stream().filter(fc -> !myChunks.contains(fc)).collect(Collectors.toList());
        if (missingChunks.isEmpty()) {
            System.out.println("Todos os pedaços já adquiridos!");
            return null;
        }
        return missingChunks;
    }
    private boolean getNextRareChunk(List<String> missingChunks) throws IOException {
        String rareChunk = Utils.findLeastFrequentChunk(new ArrayList<>(tracker.getPeerTable().values()), missingChunks);
        List<String> peerList = tracker.getPeerTable().entrySet().stream().filter(s -> s.getValue().contains(rareChunk)).map(Map.Entry::getKey).collect(Collectors.toList());
        for (String peer : peerList) {
            String peerUrlGetChunk = String.format("http://%s:8002/server/%s", peer, rareChunk);
            HttpResponse response = Utils.httpGet(peerUrlGetChunk);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    Path filePath = Paths.get(PropertiesService.peerFileFolders, rareChunk);
                    if(!Files.exists(filePath.getParent())) {
                        Files.createDirectories(filePath);
                    }
                    Files.copy(inputStream, filePath, REPLACE_EXISTING);
                } catch (Exception e) {
                    System.out.println("Erro ao obter pedaço de arquivo " + rareChunk + " Mensagem: " + e.getMessage());
                }
                return true;
            } else {
                System.out.println("Falha ao obter pedaço de arquivo: " + rareChunk);
            }
        }
        return false;
    }

    public void sendInfoToTracker(long sleepTime) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        System.out.printf("Initializing thread to send info to trackers with delay of %d Seconds\n", sleepTime);
        executor.scheduleAtFixedRate(PeerClientService::sendInfoToTracker, 0, sleepTime, TimeUnit.SECONDS);
    }

    public TrackerDto getTracker() {
        return tracker;
    }

    public void setTracker(TrackerDto tracker) {
        this.tracker = tracker;
    }

}
