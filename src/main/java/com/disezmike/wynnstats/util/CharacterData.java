package com.disezmike.wynnstats.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CharacterData {
    public String playerName;
    public List<com.google.gson.JsonObject> equippedItems = new java.util.ArrayList<>();

    public Path export() {
        // TODO: Export character data to a file in JSON format
        try {
            // ใช้พิกัดที่ปลอดภัยในโฟลเดอร์เกม
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
        }

    }
}
