package com.disezmike.wynnstats.model;

import java.util.List;
import java.util.Map;

public record ItemStatsAnalysis(
        String itemName,
        String itemTier,
        Map<String, String> baseStats,
        Map<String, Integer> identifications,
        List<String> majorIds
    ) {
}