package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LargeOceanFeature extends Feature<NoneFeatureConfiguration> {
    private static final int SEA_LEVEL = 63; // Sea level height
    private static final int OCEAN_DEPTH = 300; // Increased maximum depth of the ocean
    private static final int OCEAN_RADIUS = 1000; // Radius of the ocean

    public LargeOceanFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@SuppressWarnings("null") FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        generateOcean(world, pos, random);
        return true;
    }

    private void generateOcean(WorldGenLevel world, BlockPos pos, RandomSource random) {
        int oceanRadius = OCEAN_RADIUS; // Fixed radius for simplicity

        for (int x = -oceanRadius; x <= oceanRadius; x++) {
            for (int z = -oceanRadius; z <= oceanRadius; z++) {
                if (x * x + z * z <= oceanRadius * oceanRadius) { // Check if within circular area
                    for (int y = SEA_LEVEL; y > SEA_LEVEL - OCEAN_DEPTH; y--) {
                        BlockPos waterPos = pos.offset(x, y - SEA_LEVEL, z);
                        world.setBlock(waterPos, Blocks.WATER.defaultBlockState(), 2); // Set water block
                    }
                }
            }
        }
    }
}
