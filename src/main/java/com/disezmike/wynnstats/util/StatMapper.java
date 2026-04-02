package com.disezmike.wynnstats.util;

public class StatMapper {
    public static String toCamelCase(String loreName) {
        String[] words = loreName.split(" ");
        StringBuilder sb = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            sb.append(words[i].substring(0, 1).toUpperCase())
                    .append(words[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }
}
