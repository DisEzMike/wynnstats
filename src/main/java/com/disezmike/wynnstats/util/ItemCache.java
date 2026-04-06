package com.disezmike.wynnstats.util;

import com.disezmike.wynnstats.model.ItemData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ItemCache {
    private static final String API_URL = "https://cdn.wynntils.com/static/Reference/gear.json";
    private static final Path CACHE_PATH = Paths.get("config/wynnstats/item_cache.json");
    private static final Gson GSON = new Gson();
    private static final Map<String, JsonObject> ITEM_MAP = new HashMap<>();

    public static void initialize() {
        try {
            if (Files.exists(CACHE_PATH)) {
                loadFromLocal();
            } else {
                downloadAndCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadAndCache() throws Exception {
        System.out.println("[Wynnstats] Downloading item database...");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Files.createDirectories(CACHE_PATH.getParent());
        Files.writeString(CACHE_PATH, response.body());

        parseJson(response.body());
    }

    private static void loadFromLocal() throws Exception {
        System.out.println("[Wynnstats] Loading items from local cache...");
        String content = Files.readString(CACHE_PATH);
        parseJson(content);
    }

    private static void parseJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        root.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonObject()) {
                ITEM_MAP.put(entry.getKey(), entry.getValue().getAsJsonObject());
            }
        });
    }

    public static JsonObject get(String itemName) {
        return ITEM_MAP.get(itemName);
    }

    public static ItemData getTyped(String itemName) {
        JsonObject item = ITEM_MAP.get(itemName);
        if (item == null) {
            return null;
        }

        return GSON.fromJson(item, ItemData.class);
    }
}