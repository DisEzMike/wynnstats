package com.disezmike.wynnstats.model;

import java.util.Map;

public record ItemStatsAnalysis(
        String itemName,
        String type,
        String subType,
        String tier,
        String majorIds,
        Map<String, Integer> identifications
    ) {
}