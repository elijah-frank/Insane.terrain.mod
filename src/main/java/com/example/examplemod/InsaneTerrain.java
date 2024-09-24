package com.example.examplemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(InsaneTerrain.MODID)
public class InsaneTerrain {
    public static final String MODID = "insane_terrain";
    private static final Logger LOGGER = LogManager.getLogger();

    public InsaneTerrain(IEventBus modEventBus) {
        // Register ourselves for Forge events
        NeoForge.EVENT_BUS.register(this);
        LOGGER.info("InsaneTerrain mod initialized");
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        LOGGER.info("World load event fired");
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel level = (ServerLevel) event.getLevel();
            LOGGER.info("Server level loaded: " + level.dimension().location());
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            // The sendChat method is undefined for ServerGamePacketListenerImpl
            // We should use sendSystemMessage instead, which doesn't have a boolean parameter
            player.sendSystemMessage(Component.literal("Hello World!"));
            LOGGER.info("Player " + player.getName().getString() + " said: Hello World!");
        }
    }
}
