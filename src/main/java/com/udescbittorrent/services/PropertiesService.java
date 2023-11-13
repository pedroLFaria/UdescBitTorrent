package com.udescbittorrent.services;

public class PropertiesService {
    public static String userName = System.getProperty("peer.username");
    public static String profileName = System.getProperty("profile");
    public static String trackerAddress = System.getProperty("tracker.url");

    static public String toStrings() {
        return String.format("peer.files.folder: %s;\nprofile: %s;\ntracker.url: %s;\n", userName, profileName, trackerAddress);
    }
}
