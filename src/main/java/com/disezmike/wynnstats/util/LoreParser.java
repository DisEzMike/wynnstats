package com.disezmike.wynnstats.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.disezmike.wynnstats.model.ItemStatsAnalysis;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ItemLore;

public class LoreParser {
    private static final String CLEANUP_REGEX = "§.|[^\\x00-\\x7F]";

    private static final Pattern IDENTIFICATION_PATTERN = Pattern.compile("^([a-zA-Z\\s]+?)\\s*([+-])([\\d,]+)");
    private static final Pattern MARJOR_ID_PATTERN = Pattern.compile("^([a-zA-Z\\s]+?)\\s*:");

    public static ItemStatsAnalysis parse(String name, ItemLore lore) {
        Map<String, Integer> ids = new HashMap<>();
        String majorIds = "";

        String itemName = clean(name);
        JsonObject itemCache = ItemCache.get(itemName);

        // cache lookup
        String type = itemCache.get("type").getAsString();
        String subType = itemCache.get("subType").getAsString();
        String tier = itemCache.get("tier").getAsString();

        for (Component line : lore.lines()) {
            String text = clean(line.getString());

            if (text.isEmpty())
                continue;

            // Identifications
            if (isIdentification(text)) {
                parseIdentification(text, ids, itemCache);
            }

            // Major IDs
            else if (isMajorId(text)) {
                majorIds = parseMajorId(text);
            }
        }

        System.out.println("Parsed Item: " + itemName);
        System.out.println("Ids: " + ids);

        return new ItemStatsAnalysis(itemName, type, subType, tier, majorIds, ids);
    }

    // Helper method

    private static String clean(String text) {
        return text.replaceAll(CLEANUP_REGEX, "").trim();
    }

    private static boolean isIdentification(String line) {
        Matcher m = IDENTIFICATION_PATTERN.matcher(line);
        return m.find();
    }

    private static void parseIdentification(String text, Map<String, Integer> idsMap, JsonObject itemCache) {
        Matcher m = IDENTIFICATION_PATTERN.matcher(text);
        if (m.find()) {
            JsonElement identifications = itemCache.get("identifications");
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            try {
                Number parsedNumber = format.parse(m.group(3).replaceAll(",", ""));
                int value = parsedNumber.intValue();
                String statName = m.group(1);

                List<String> alliesOfKey = List.of(StatMapper.toCamelCase("raw " + statName),
                        StatMapper.toCamelCase(statName + " raw"), StatMapper.toCamelCase(statName));

                for (String key : alliesOfKey) {
                    if (identifications != null && identifications.getAsJsonObject().has(key)) {
                        statName = key;
                        break;
                    }
                }

                if (!statName.isEmpty() && !statName.contains(":")) {
                    idsMap.put(statName, value);
                }
            } catch (java.text.ParseException e) {
                System.err.println("Failed to parse number: " + m.group(3));
            }
        }
    }

    private static boolean isMajorId(String line) {
        Matcher m = MARJOR_ID_PATTERN.matcher(line);
        return m.find();
    }

    private static String parseMajorId(String text) {
        Matcher m = MARJOR_ID_PATTERN.matcher(text);
        if (m.find()) {
            return m.group(1).trim();
        }
        return "";
    }

    private static void rawParse(String text) {
        // For debugging purposes
        System.out.println(text);
    }
}