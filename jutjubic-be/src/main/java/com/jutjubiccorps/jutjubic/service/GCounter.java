package com.jutjubiccorps.jutjubic.service;

import java.util.HashMap;
import java.util.Map;

public class GCounter {
    // count po replici
    private Map<String, Integer> counts = new HashMap<>();

    public void increment(String replicaId) {
        counts.put(replicaId, counts.getOrDefault(replicaId, 0) + 1);
    }

    public int value() {
        return counts.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void merge(GCounter other) {
        other.counts.forEach((replicaId, count) ->
                counts.put(replicaId, Math.max(counts.getOrDefault(replicaId, 0), count))
        );
    }
}

