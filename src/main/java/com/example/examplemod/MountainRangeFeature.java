package com.example.examplemod;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
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
    public boolean place(@SuppressWarnings("null") @NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();

        int range = 150;
        int baseHeight = 80;
        int maxHeight = 500;

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                double distanceFromCenter = Math.sqrt(x * x + z * z) / range;
                double heightFactor = 1 - Math.pow(distanceFromCenter, 2);

                if (heightFactor > 0) {
                    int peakHeight = baseHeight + (int) (heightFactor * (maxHeight - baseHeight));
                    generateMountainColumn(world, pos.offset(x, 0, z), baseHeight, peakHeight);
                }
            }
        }
        return true;
    }

    private void generateMountainColumn(WorldGenLevel world, BlockPos pos, int baseHeight, int peakHeight) {
        for (int y = baseHeight; y <= peakHeight; y++) {
            BlockPos blockPos = pos.above(y - baseHeight);
            if (y == peakHeight) {
                world.setBlock(blockPos, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
            } else if (y > peakHeight - 5) {
                world.setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 2);
            } else {
                world.setBlock(blockPos, Blocks.STONE.defaultBlockState(), 2);
            }
        }
    }
}
