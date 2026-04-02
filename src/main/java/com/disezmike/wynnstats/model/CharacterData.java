package com.disezmike.wynnstats.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CharacterData {
    @SuppressWarnings("unused")
    private String playerName;
    private List<ItemStatsAnalysis> build;

    public CharacterData(String playerName) {
        this.playerName = playerName;
        this.build = new ArrayList<>();
    }

    public void addBuildItem(ItemStatsAnalysis itemStats) {
        this.build.add(itemStats);
    }

    private void clearBuild() {
        this.build.clear();
    }

    public Path export() {
        try {
            Path path = Paths.get("exports/wynnstats_export.json").toAbsolutePath();
            Files.createDirectories(path.getParent());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);

            Files.writeString(path, json);
            System.out.println("Successfully exported to: " + path.toAbsolutePath());

            return path.toAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            // Clear build data from memory after export
            this.clearBuild();
        }
    }
}
