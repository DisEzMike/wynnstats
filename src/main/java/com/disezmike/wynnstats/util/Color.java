package com.disezmike.wynnstats.util;

public enum Color {
    AQUA("§b"),
    BLACK("§0"),
    BLUE("§9"),
    DARK_AQUA("§3"),
    DARK_BLUE("§1"),
    DARK_GRAY("§8"),
    DARK_GREEN("§2"),
    DARK_PURPLE("§5"),
    DARK_RED("§4"),
    GOLD("§6"),
    GRAY("§7"),
    GREEN("§a"),
    LIGHT_PURPLE("§d"),
    RED("§c"),
    WHITE("§f"),
    YELLOW("§e"),
    NORMAL("§r");

    private final String code;

    Color(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
