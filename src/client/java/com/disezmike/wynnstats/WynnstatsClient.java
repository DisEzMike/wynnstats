package com.disezmike.wynnstats;

import java.nio.file.Path;

import com.disezmike.wynnstats.util.CharacterData;
import com.disezmike.wynnstats.util.ItemCache;
import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class WynnstatsClient implements ClientModInitializer {
	@Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        // TODO: Add command "/wynnstat"
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(
                    // Main Command
                    ClientCommandManager.literal("wynnstats")
                            .executes(context -> {

                                LocalPlayer player = context.getSource().getPlayer();
                                if (player != null) {
                                    String playerName = player.getGameProfile().name();
                                    CharacterData characterData = new CharacterData();
                                    characterData.playerName = playerName;

                                    // find gear in hotbar, accessory and armor slots, then print rarity if found in cache
                                    this._findGear(context, characterData);


                                    // Save to file
                                    Path exportFilePath = characterData.export().getParent();
                                    if (exportFilePath != null) {
                                        Component msg = Component.literal("§a[Wynnstats] Export successful!");
                                        Component clickMsg = Component.literal("§7Click to open file")
                                            .withStyle(style -> 
                                                style.withUnderlined(true)
                                                    .withClickEvent(new ClickEvent.OpenFile(exportFilePath))
                                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("Open " + exportFilePath.toString())))
                                                );
                                        context.getSource().sendFeedback(msg);
                                        context.getSource().sendFeedback(clickMsg);
                                    } else {
                                        context.getSource().sendError(Component.literal("§cFailed to export character data."));
                                    }
                                }
                                return 1;
                            }));
        });
    }
    
    private int _findGear(CommandContext<FabricClientCommandSource> context, CharacterData characterData) {
        LocalPlayer player = context.getSource().getPlayer();
        if (player == null)
            return 1;

        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            // process only hotbar, accessory and armor slots, otherwise skip
            if (itemStack.isEmpty() || !this._isGear(i))
                continue;

            String itemName = itemStack.getHoverName().getString();
            JsonObject data = ItemCache.get(itemName);

            if (data != null) {
                characterData.equippedItems.add(data);
            }
        }
        return 1;
    }

    private boolean _isGear(int slot) {
        boolean isMainHand = slot == 0;
        boolean accessory = slot == 9 || slot == 10 || slot == 11 || slot == 12;
        boolean armor = slot == 36 || slot == 37 || slot == 38 || slot == 39;
        return isMainHand || accessory || armor;
    }
}