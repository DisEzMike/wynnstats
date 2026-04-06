package com.disezmike.wynnstats.util;

public class StatMapper {
    public static String toCamelCase(String loreName) {
        if (loreName == null || loreName.trim().isEmpty()) {
            return "";
        }

        String[] words = loreName.trim().split("\\s+");

        if (words.length == 0 || words[0].isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {

            if (words[i].isEmpty())
                continue;

            sb.append(words[i].substring(0, 1).toUpperCase())
                    .append(words[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }
}
