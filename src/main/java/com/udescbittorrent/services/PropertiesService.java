package com.udescbittorrent.services;

public class PropertiesService {
    public static String peerFileFolders = System.getProperty("peer.files.folder");
    public static String profileName = System.getProperty("profile");
    public static String trackerAddress = System.getProperty("tracker.url");
    public static String peerThreadSleepTime = System.getProperty("peer.thread.sleep-time");
    
    static public String toStrings() {
        return String.format("peer.files.folder: %s;\nprofile: %s;\ntracker.url: %s;\npeer.thread.sleep-time: %s", peerFileFolders, profileName, trackerAddress, peerThreadSleepTime);
    }
}
