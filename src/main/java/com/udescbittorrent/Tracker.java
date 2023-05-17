package com.udescbittorrent;

import java.util.BitSet;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Tracker {
    private Map<Peer, BitSet> peerPieces = new ConcurrentHashMap<>();

    public void registerPeer(Peer peer, BitSet pieces) {
        peerPieces.put(peer, pieces);
    }

    public Map<Peer, BitSet> getPeers() {
        return Collections.unmodifiableMap(peerPieces);
    }
}
