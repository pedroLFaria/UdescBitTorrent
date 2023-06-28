package com.udescbittorrent.peerHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udescbittorrent.ObjectMapperSingleton;
import com.udescbittorrent.PropertiesService;
import com.udescbittorrent.models.TrackerDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PeerClient {

    static ObjectMapper mapper = ObjectMapperSingleton.getInstance();
    static Peer peerService = Peer.getInstance();
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void sendInfoToTracker() throws IOException {
        List<String> fileChunks =  getFileChunks();
        TrackerDto trackerDto = httpPost(PropertiesService.trackerUrl, fileChunks);
        peerService.initiateChunkMap(trackerDto.getFileChunks());
        trackerDto.getPeerDtoList().forEach(peerDto -> {
            peerDto.getFileChunk().forEach(chunk -> peerService.addPeerToChunkMap(chunk, peerDto.getIp()));
        });
        peerService.setPeers(trackerDto.getPeerDtoList());
        peerService.setFilesChunksInfo(trackerDto.getFileChunks());
    }

    public static void getPeersFromTracker() throws IOException {
        HttpResponse response = httpGet(PropertiesService.trackerUrl);
        TrackerDto trackerDto = mapperToDto(response);
        peerService.setPeers(trackerDto.getPeerDtoList());
        peerService.setFilesChunksInfo(trackerDto.getFileChunks());
    }

    static TrackerDto httpPost(String url, Object fileChunks) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(mapper.writeValueAsString(fileChunks)));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(httpPost);
        return mapperToDto(response);
    }

    static HttpResponse httpGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        return response;
    }

    private static TrackerDto mapperToDto(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new IOException("Sem retorno do tracker");
        }
        String resultString = EntityUtils.toString(entity);
        return mapper.readValue(resultString, TrackerDto.class);
    }

    public static TrackerDto getNextFileChunkFromPeer() throws IOException {
        List<String> myChunks = getFileChunks();
        List<String> missingChunks = peerService.getFilesChunksInfo().stream().filter(myChunks::contains).collect(Collectors.toList());
        String rareChunk = peerService.getChunkToPeerMap().keySet().stream().min(chunkToPeerMapComparator).orElseThrow(RuntimeException::new);
        List<String> peerList = peerService.getChunkToPeerMap().get(rareChunk);
        for (String peer: peerList) {
            HttpResponse response = httpGet(String.format("http://%s/%s", peer, rareChunk));
        }
        return null;

    }

    public static List<String> getFileChunks() {
        List<String> fileList = new ArrayList<>();
        String folderPath = PropertiesService.peerFileFolders;
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file.getName());
                    }
                }
            }
        }

        return fileList;
    }
    static Comparator<String> chunkToPeerMapComparator = new Comparator<String>() {
        @Override
        public int compare(String chunk1, String chunk2) {
            int chunk1Size = peerService.getChunkToPeerMap().get(chunk1).size();
            int chunk2Size = peerService.getChunkToPeerMap().get(chunk2).size();
            return chunk1Size - chunk2Size;
        }
    };
}
