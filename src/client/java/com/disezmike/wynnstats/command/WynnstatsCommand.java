package com.disezmike.wynnstats.command;

import java.nio.file.Path;

import org.slf4j.Logger;

import com.disezmike.wynnstats.model.CharacterData;
import com.disezmike.wynnstats.model.ItemStatsAnalysis;
import com.disezmike.wynnstats.model.Message;
import com.disezmike.wynnstats.model.MessageType;
import com.disezmike.wynnstats.util.Color;
import com.disezmike.wynnstats.util.FeedbackHelper;
import com.disezmike.wynnstats.util.LoreParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public class WynnstatsCommand {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(WynnstatsCommand::registerCommands);
    }

    private static void registerCommands(
            CommandDispatcher<FabricClientCommandSource> dispatcher,
            CommandBuildContext registeryAccess) {
        LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommandManager.literal("wynnstats")
                .executes(context -> {
                    LocalPlayer player = context.getSource().getPlayer();

                    if (player == null) {
                        Message message = new Message(
                                "Error",
                                new Component[] {
                                        Message.line("Player not found", Color.RED)
                                });
                        FeedbackHelper.sendFeedback(context, message, MessageType.ERROR);
                        return 0;
                    }

                    String playerName = player.getGameProfile().name();
                    CharacterData characterData = new CharacterData(playerName);

                    Inventory inventory = player.getInventory();

                    for (int i = 0; i < inventory.getContainerSize(); i++) {
                        ItemStack itemStack = inventory.getItem(i);

                        if (itemStack.isEmpty() || !_isSlotGear(i))
                            continue;

                        String itemName = itemStack.getHoverName().getString();
                        ItemLore itemLore = itemStack.get(DataComponents.LORE);
                        ItemStatsAnalysis itemStats = LoreParser.parse(itemName, itemLore);
                        characterData.addBuildItem(itemStats);
                    }

                    // Save to file
                    Path exportFilePath = characterData.export().getParent();
                    if (exportFilePath != null) {
                        Message message = new Message(
                                "Export successful",
                                new Component[] {
                                        Message.line("Successfully exported character data!", Color.GREEN),
                                        Message.file("Click to open file", exportFilePath, Color.GRAY)
                                });
                        FeedbackHelper.sendFeedback(context, message, MessageType.SUCCESS);
                    } else {
                        Message message = new Message(
                                "Export failed",
                                new Component[] {
                                        Message.line("Failed to export character data.", Color.RED)
                                });
                        FeedbackHelper.sendFeedback(context, message, MessageType.ERROR);
                    }

                    return 1;
                });

        dispatcher.register(root);
    }

    private static boolean _isSlotGear(int slot) {
        boolean isMainHand = slot == 0;
        boolean accessory = slot == 9 || slot == 10 || slot == 11 || slot == 12;
        boolean armor = slot == 36 || slot == 37 || slot == 38 || slot == 39;
        return isMainHand || accessory || armor;
    }
}
