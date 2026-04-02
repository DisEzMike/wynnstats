package com.disezmike.wynnstats.client;

import com.disezmike.wynnstats.command.WynnstatsCommand;

import net.fabricmc.api.ClientModInitializer;

public class WynnstatsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WynnstatsCommand.register();
    }
    
}
