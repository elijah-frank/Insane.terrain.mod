package com.example.examplemod;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EnhancedOverworldFeature extends Feature<NoneFeatureConfiguration> {
    private static final int SEA_LEVEL = -62;
    private static final int CLOUD_LEVEL = 128;

    public EnhancedOverworldFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@SuppressWarnings("null") FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        // Generate mountains
        generateMountains(world, pos, random);

        // Generate oceans
        generateOceans(world, pos, random);

        return true;
    }

    private void generateMountains(WorldGenLevel world, BlockPos pos, RandomSource random) {
        // Mountain generation logic
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                int height = random.nextInt(20) + 10;
                for (int y = SEA_LEVEL; y <= SEA_LEVEL + height; y++) {
                    BlockPos blockPos = pos.offset(x, y, z);
                    if (y > SEA_LEVEL + height / 2) {
                        world.setBlock(blockPos, Blocks.TERRACOTTA.defaultBlockState(), 2);
                    } else if (y > CLOUD_LEVEL) {
                        world.setBlock(blockPos, Blocks.SNOW_BLOCK.defaultBlockState(), 2);
                    } else {
                        world.setBlock(blockPos, Blocks.STONE.defaultBlockState(), 2);
                    }
                }
                if (SEA_LEVEL + height > CLOUD_LEVEL) {
                    world.setBlock(pos.offset(x, SEA_LEVEL + height + 1, z), Blocks.SNOW.defaultBlockState(), 2);
                } else {
                    world.setBlock(pos.offset(x, SEA_LEVEL + height + 1, z), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                }
            }
        }
    }

    private void generateOceans(WorldGenLevel world, BlockPos pos, RandomSource random) {
        // Ocean generation logic
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                if (x * x + z * z > 400) {
                    continue;
                }
                for (int y = SEA_LEVEL + 1; y <= SEA_LEVEL + 10; y++) {
                    BlockPos terrainPos = pos.offset(x, y, z);
                    if (world.getBlockState(terrainPos).is(Blocks.STONE) || world.getBlockState(terrainPos).is(Blocks.DIRT)) {
                        world.setBlock(terrainPos, Blocks.SAND.defaultBlockState(), 2);
                    }
                }
            }
        }
    }
}
