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
    private static final int SEA_LEVEL = 0;
    private static final int MAX_HEIGHT = 600;
    private static final int CLOUD_LEVEL = 205; // Cloud level at Y=150
    private static final int LAKE_CHANCE = 10; // 1 in 10 chance for a lake
    private static final int TERRACOTTA_CHANCE = 20; // 1 in 5 chance for terracotta cliffs
    private static final int SNOWY_PEAK_CHANCE = 30; // 1 in 3 chance for snowy peaks

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
        int peakHeight = SEA_LEVEL + 200 + random.nextInt(MAX_HEIGHT - 200); // Mountains start at Y=200 up to Y=600
        int radius = 20 + random.nextInt(40);

        boolean hasTerracottaCliffs = random.nextInt(100) < TERRACOTTA_CHANCE;
        boolean hasSnowyPeaks = random.nextInt(100) < SNOWY_PEAK_CHANCE;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    int height = peakHeight - (int) Math.sqrt(x * x + z * z) * (peakHeight - SEA_LEVEL) / radius;
                    for (int y = SEA_LEVEL; y <= height; y++) {
                        if (hasTerracottaCliffs && y > SEA_LEVEL + (height - SEA_LEVEL) / 2) {
                            world.setBlock(pos.offset(x, y - SEA_LEVEL, z), Blocks.TERRACOTTA.defaultBlockState(), 2);
                        } else if (hasSnowyPeaks && y > CLOUD_LEVEL) { // Only add snow if above cloud level
                            world.setBlock(pos.offset(x, y - SEA_LEVEL, z), Blocks.SNOW_BLOCK.defaultBlockState(), 2);
                        } else {
                            world.setBlock(pos.offset(x, y - SEA_LEVEL, z), Blocks.STONE.defaultBlockState(), 2);
                        }
                    }
                    if (hasSnowyPeaks && height > CLOUD_LEVEL) { // Only add snow layer if above cloud level
                        world.setBlock(pos.offset(x, height - SEA_LEVEL + 1, z), Blocks.SNOW.defaultBlockState(), 2);
                    } else {
                        world.setBlock(pos.offset(x, height - SEA_LEVEL + 1, z), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                    }
                }
            }
        }

        if (random.nextInt(LAKE_CHANCE) == 0) {
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
                        if (isSnowy && pos.getY() > CLOUD_LEVEL) {
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
