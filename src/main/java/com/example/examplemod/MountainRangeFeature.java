package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MountainRangeFeature extends Feature<NoneFeatureConfiguration> {
    public MountainRangeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@SuppressWarnings("null") FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        ChunkGenerator generator = context.chunkGenerator();
        BlockPos pos = context.origin();

        // Simple logic to create a mountain range
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                int height = (int) (Math.sin(x / 10.0) * Math.cos(z / 10.0) * 20 + 64);
                for (int y = 64; y <= height; y++) {
                    world.setBlock(pos.offset(x, y, z), Blocks.STONE.defaultBlockState(), 2);
                }
            }
        }
        return true;
    }
}
