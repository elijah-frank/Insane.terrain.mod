package com.example.examplemod;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LargeOceanFeature extends Feature<NoneFeatureConfiguration> {

    public LargeOceanFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        if (random.nextInt(100) < 20) { // 20% chance to generate ocean
            generateOcean(world, pos, random);
        }
        return true;
    }

    private void generateOcean(WorldGenLevel world, BlockPos pos, RandomSource random) {
        int oceanRadius = Constants.OCEAN_RADIUS; // Fixed radius for simplicity

        for (int x = -oceanRadius; x <= oceanRadius; x++) {
            for (int z = -oceanRadius; z <= oceanRadius; z++) {
                if (x * x + z * z <= oceanRadius * oceanRadius) { // Check if within circular area
                    for (int y = Constants.SEA_LEVEL; y > Constants.SEA_LEVEL - Constants.OCEAN_DEPTH; y--) {
                        BlockPos waterPos = pos.offset(x, y - Constants.SEA_LEVEL, z);
                        world.setBlock(waterPos, Blocks.WATER.defaultBlockState(), 2); // Set water block
                    }
                }
            }
        }

        // Adjust surrounding terrain to match the new sea level
        adjustSurroundingTerrain(world, pos, oceanRadius);
    }

    private void adjustSurroundingTerrain(WorldGenLevel world, BlockPos pos, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z > radius * radius) {
                    continue;
                }

                for (int y = Constants.SEA_LEVEL + 1; y <= Constants.SEA_LEVEL + 10; y++) {
                    BlockPos terrainPos = pos.offset(x, y, z);
                    if (world.getBlockState(terrainPos).is(Blocks.STONE) || world.getBlockState(terrainPos).is(Blocks.DIRT)) {
                        world.setBlock(terrainPos, Blocks.SAND.defaultBlockState(), 2);
                    }
                }
            }
        }
    }
}