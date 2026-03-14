package com.disezmike.wynnstats.util;

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
    private static final String API_URL = "https://api.wynncraft.com/v3/item/database?fullResult";
    private static final Path CACHE_PATH = Paths.get("config/wynnstats/item_cache.json");
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

        // สร้างโฟลเดอร์ถ้ายังไม่มี
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
        // Wynncraft API v3 เก็บไอเทมไว้ในฟิลด์ที่ชื่อว่า "items" หรือเป็น Map โดยตรง
        // หมายเหตุ: คุณต้องเช็กโครงสร้าง JSON จริงจาก API อีกทีนะครับ
        root.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonObject()) {
                ITEM_MAP.put(entry.getKey(), entry.getValue().getAsJsonObject());
            }
        });
    }

    public static JsonObject get(String itemName) {
        return ITEM_MAP.get(itemName);
    }
}