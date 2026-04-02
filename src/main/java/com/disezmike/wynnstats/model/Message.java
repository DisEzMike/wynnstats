package com.disezmike.wynnstats.model;

import java.net.URI;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

import com.disezmike.wynnstats.util.Color;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

public record Message(String title, Component[] content) {

    // Helper : formats a line of text with the specified color
    public static Component line(String text, Color color) {
        return Component.literal(color.getCode() + text);
    }

    
    // Helper : formats a label and value pair with the value in the specified color
    public static Component mixed(String label, String value, Color valueColor) {
        return Component.literal(label + valueColor.getCode() + value);
    }

    // Helper : formats a clickable link with the specified color
    public static Component link(String text, String url, @NotNull Color color) {
        URI uri = URI.create(url);
        if (uri.getScheme() == null || (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))) {
            throw new IllegalArgumentException("URL must start with http:// or https://");
        }
        return Component.literal(color.getCode() + text)
                .withStyle(style -> style.withUnderlined(true)
                        .withClickEvent(new ClickEvent.OpenUrl(uri))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Open " + url))));
    }
    
    public static Component file(String text, Path path, @NotNull Color color) {
        return Component.literal(color.getCode() + text)
                .withStyle(style -> style.withUnderlined(true)
                        .withClickEvent(new ClickEvent.OpenFile(path))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Open " + path.toString()))));
    }
}
