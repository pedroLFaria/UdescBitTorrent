package com.udescbittorrent;

public class PropertiesService {
    public static String peerFileFolders = System.getProperty("peer.files.folder");
    public static String profileName = System.getProperty("profile");
    public static String trackerAddress = System.getProperty("tracker.url");
    public static String peerThreadSleepTime = System.getProperty("peer.thread.sleep-time");
    public static String fileChunks = System.getProperty("file-chunks");
// -Dfile-chunks=1-file.txt,2-file.txt,3-file.txt
    static public String toStrings() {
        return String.format("peer.files.folder: %s;\nprofile: %s;\ntracker.url: %s;\npeer.thread.sleep-time: %s", peerFileFolders, profileName, trackerAddress, peerThreadSleepTime);
    }
}
