package com.disezmike.wynnstats.util;

import com.disezmike.wynnstats.model.Message;
import com.disezmike.wynnstats.model.MessageType;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class FeedbackHelper {
    public static void sendFeedback(CommandContext<FabricClientCommandSource> context, Message message, MessageType type) {
        String prefix = switch (type) {
            case INFO -> "§b[Wynnstats]";
            case SUCCESS -> "§a[Wynnstats]";
            case ERROR -> "§c[Wynnstats]";
        };

        String title = prefix + " " + message.title();
        context.getSource().sendFeedback(net.minecraft.network.chat.Component.literal(title));

        for (Component line : message.content()) {
            if (line.getString().trim().isEmpty()) continue;
            context.getSource().sendFeedback(line);
        }
    }
}
