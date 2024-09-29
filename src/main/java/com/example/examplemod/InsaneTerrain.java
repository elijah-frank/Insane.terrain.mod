package com.example.examplemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(InsaneTerrain.MODID)
public class InsaneTerrain {
    public static final String MODID = "insane_terrain";
    private static final Logger LOGGER = LogManager.getLogger();

    public InsaneTerrain(IEventBus modEventBus) {
        LOGGER.info("InsaneTerrain mod initialized");
        
        // Register the features to the mod event bus
        ModFeatures.registerFeatures(modEventBus);
    }
}