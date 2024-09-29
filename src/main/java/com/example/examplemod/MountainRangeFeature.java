package com.example.examplemod;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MountainRangeFeature extends Feature<NoneFeatureConfiguration> {

    public MountainRangeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        generateMountain(world, pos, random);
        return true;
    }

    private void generateMountain(WorldGenLevel world, BlockPos pos, RandomSource random) {
        int peakHeight = Constants.SEA_LEVEL + Constants.MOUNTAIN_MIN_HEIGHT + random.nextInt(Constants.MOUNTAIN_MAX_HEIGHT - Constants.MOUNTAIN_MIN_HEIGHT);
        int radius = 20 + random.nextInt(40);

        boolean hasTerracottaCliffs = random.nextInt(100) < Constants.TERRACOTTA_CHANCE;
        boolean hasSnowyPeaks = random.nextInt(100) < Constants.SNOWY_PEAK_CHANCE;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    int height = peakHeight - (int) Math.sqrt(x * x + z * z) * (peakHeight - Constants.SEA_LEVEL) / radius;
                    for (int y = Constants.SEA_LEVEL; y <= height; y++) {
                        if (hasTerracottaCliffs && y > Constants.SEA_LEVEL + (height - Constants.SEA_LEVEL) / 2) {
                            world.setBlock(pos.offset(x, y - Constants.SEA_LEVEL, z), Blocks.TERRACOTTA.defaultBlockState(), 2);
                        } else if (hasSnowyPeaks && y > Constants.CLOUD_LEVEL) { // Only add snow if above cloud level
                            world.setBlock(pos.offset(x, y - Constants.SEA_LEVEL, z), Blocks.SNOW_BLOCK.defaultBlockState(), 2);
                        } else {
                            world.setBlock(pos.offset(x, y - Constants.SEA_LEVEL, z), Blocks.STONE.defaultBlockState(), 2);
                        }
                    }
                    if (hasSnowyPeaks && height > Constants.CLOUD_LEVEL) { // Only add snow layer if above cloud level
                        world.setBlock(pos.offset(x, height - Constants.SEA_LEVEL + 1, z), Blocks.SNOW.defaultBlockState(), 2);
                    } else {
                        world.setBlock(pos.offset(x, height - Constants.SEA_LEVEL + 1, z), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                    }
                }
            }
        }

        if (random.nextInt(Constants.LAKE_CHANCE) == 0) {
            generateMountainTopLake(world, new BlockPos(pos.getX(), peakHeight, pos.getZ()), random, hasSnowyPeaks);
        }
    }

    private void generateMountainTopLake(WorldGenLevel world, BlockPos pos, RandomSource random, boolean isSnowy) {
        int lakeRadius = 5 + random.nextInt(5);
        int lakeDepth = 3 + random.nextInt(3);

        for (int x = -lakeRadius; x <= lakeRadius; x++) {
            for (int z = -lakeRadius; z <= lakeRadius; z++) {
                if (x * x + z * z <= lakeRadius * lakeRadius) {
                    for (int y = 0; y >= -lakeDepth; y--) {
                        if (isSnowy && pos.getY() > Constants.CLOUD_LEVEL) {
                            world.setBlock(pos.offset(x, y, z), y == 0 ? Blocks.ICE.defaultBlockState() : Blocks.SNOW_BLOCK.defaultBlockState(), 2);
                        } else {
                            world.setBlock(pos.offset(x, y, z), y == 0 ? Blocks.WATER.defaultBlockState() : Blocks.STONE.defaultBlockState(), 2);
                        }
                    }
                }
            }
        }
    }
}