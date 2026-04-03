package com.disezmike.wynnstats.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.disezmike.wynnstats.model.ItemStatsAnalysis;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ItemLore;

public class LoreParser {
    private static final String CLEANUP_REGEX = "§.|[^\\x00-\\x7F]";

    private static final Pattern DIGIT_PATTERN = Pattern.compile("([+-]?\\d+)");

    public static ItemStatsAnalysis parse(String name, ItemLore lore) {
        Map<String, String> base = new HashMap<>();
        Map<String, Integer> ids = new HashMap<>();
        List<String> majors = new ArrayList<>();
        List<String> powderEffects = new ArrayList<>();
        String tier = "Normal";

        boolean skippingSetBonus = false;
        boolean skippingPowderEffects = false;

        for (Component line : lore.lines()) {
            String text = clean(line.getString());

            if (text.startsWith("Set Bonus:")) {
                skippingSetBonus = true;
                continue;
            }
            if (skippingSetBonus) {
                if (text.isEmpty()) {
                    skippingSetBonus = false;
                }
                continue;
            }

            // Powder Effect
            if (isPowderEffect(text)) {
                skippingPowderEffects = true;
                powderEffects.add(text);
                continue;
            }
            if (skippingPowderEffects) {
                if (text.isEmpty()) {
                    skippingPowderEffects = false;
                }
                continue;
            }

            if (text.isEmpty())
                continue;

            // Base Stats
            if (isBaseStat(text)) {
                parseBaseStat(text, base);
            }

            // Identifications
            else if (isIdentification(text)) {
                parseIdentification(text, ids);
            }

            // Major IDs
            else if (text.startsWith("+") && !text.matches(".*\\d.*")) {
                majors.add(text.replace("+", "").split(":")[0].trim());
            }

            // Item Tier
            else if (text.contains("Item")) {
                tier = text.replace("Item", "").trim();
            }
        }

        return new ItemStatsAnalysis(name, tier, base, ids, majors, powderEffects);
    }

    // Helper method

    private static String clean(String text) {
        return text.replaceAll(CLEANUP_REGEX, "").trim();
    }

    private static boolean isBaseStat(String text) {
        boolean isNotIdPrefix = !text.startsWith("+") && !text.startsWith("-") && !text.startsWith("*");
        boolean isNotRequirement = !text.contains("Min:") && !text.contains("Req:");

        return isNotIdPrefix && isNotRequirement && (text.contains("Damage:") ||
                text.contains("Health") ||
                text.contains("Defence") ||
                text.contains("Average DPS") ||
                text.contains("Attack Speed"));
    }

    private static void parseBaseStat(String text, Map<String, String> baseMap) {
        if (text.contains("Damage:")) {
            String[] parts = text.split("Damage:");
            String label = parts[0].trim() + " Damage";
            baseMap.put(StatMapper.toCamelCase(label), parts[1].trim());
        } else if (text.contains("Attack Speed")) {
            baseMap.put(StatMapper.toCamelCase("Attack Speed"), text.replace("Attack Speed", "").trim());
        } else {
            Matcher m = DIGIT_PATTERN.matcher(text);
            if (m.find()) {
                String label = text.replaceAll("[+-]?\\d+", "").replace(":", "").trim();
                baseMap.put(StatMapper.toCamelCase(label), m.group(1));
            }
        }
    }

    private static boolean isIdentification(String line) {
        return (line.startsWith("+") || line.startsWith("-") || line.startsWith("*"))
                && line.matches(".*\\d.*")
                && !line.contains("Min:")
                && !line.contains("Req:")
                && !line.contains("Damage:")
                && !line.contains("Set Bonus:");
    }

    private static void parseIdentification(String text, Map<String, Integer> idsMap) {
        Matcher m = DIGIT_PATTERN.matcher(text);
        if (m.find()) {
            int value = Integer.parseInt(m.group(1));
            String statName = text.replaceAll("^[+-]?\\d+(%|/\\d+s)?", "")
                    .replaceAll("\\*", "").trim();

            if (!statName.isEmpty() && !statName.contains(":")) {
                idsMap.put(StatMapper.toCamelCase(statName), value);
            }
        }
    }

    private static boolean isPowderEffect(String line) {
        return line.contains("Quake") || line.contains("Chain Lightning") || line.contains("Curse")
                || line.contains("Courage") || line.contains("Wind Prison")
                || line.contains("Rage") || line.contains("Kill Streak") || line.contains("Concentration")
                || line.contains("Endurance") || line.contains("Dodge");
    }
}