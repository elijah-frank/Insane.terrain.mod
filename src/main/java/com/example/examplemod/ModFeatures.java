package com.example.examplemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, InsaneTerrain.MODID);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> MOUNTAIN_RANGE_FEATURE = 
        FEATURES.register("mountain_range_feature", () -> new MountainRangeFeature());
}
