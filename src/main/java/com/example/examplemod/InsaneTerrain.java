package com.example.examplemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
// Import for FMLJavaModLoadingContext
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(InsaneTerrain.MODID)
public class InsaneTerrain {
    public static final String MODID = "insane_terrain";
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, MODID);

    public static final DeferredHolder<Feature<?>> ENHANCED_OVERWORLD_FEATURE = FEATURES.register("enhanced_overworld_feature", 
        () -> new EnhancedOverworldFeature(NoneFeatureConfiguration.CODEC));

    public InsaneTerrain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FEATURES.register(modEventBus);
        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ResourceKey<LevelStem> enhancedOverworldKey = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation(MODID, "enhanced_overworld"));

            WorldGenSettings enhancedOverworldSettings = BuiltInRegistries.WORLD_PRESET.get(new ResourceLocation(MODID, "enhanced_overworld"));
            
            // Registering LevelStem with the correct DimensionType reference
            Registry.register(
                BuiltInRegistries.LEVEL_STEM,
                enhancedOverworldKey,
                new LevelStem(
                    BuiltInRegistries.DIMENSION_TYPE.getHolderOrThrow(DimensionTypes.OVERWORLD),
                    enhancedOverworldSettings
                )
            );

            LOGGER.info("Enhanced Overworld dimension registered.");
        });
    }
}
