package com.example.examplemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, InsaneTerrain.MODID);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> MOUNTAIN_RANGE_FEATURE = 
        FEATURES.register("mountain_range_feature", MountainRangeFeature::new);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> LARGE_OCEAN_FEATURE = 
        FEATURES.register("large_ocean_feature", LargeOceanFeature::new);

    public static void registerFeatures(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
