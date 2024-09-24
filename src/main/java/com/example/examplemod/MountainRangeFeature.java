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
    private static final int SEA_LEVEL = 63;
    private static final int MAX_HEIGHT = 2000;
    private static final int VALLEY_HEIGHT_OFFSET = 100;

    public MountainRangeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@SuppressWarnings("null") @NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        int rangeLength = 500 + random.nextInt(1500);
        int rangeWidth = 100 + random.nextInt(200);

        double angle = random.nextDouble() * Math.PI * 2;
        double dx = Math.cos(angle);
        double dz = Math.sin(angle);

        BlockPos peakPos = null;
        int maxPeakHeight = 0;

        for (int i = 0; i < rangeLength; i++) {
            int x = (int) (pos.getX() + i * dx);
            int z = (int) (pos.getZ() + i * dz);
            BlockPos slicePos = new BlockPos(x, SEA_LEVEL, z);
            int peakHeight = generateMountainSlice(world, slicePos, rangeWidth, random);

            if (peakHeight > maxPeakHeight) {
                maxPeakHeight = peakHeight;
                peakPos = slicePos.above(peakHeight - SEA_LEVEL);
            }
        }

        if (peakPos != null) {
            generateRiver(world, peakPos, random);
        }

        return true;
    }

    private int generateMountainSlice(WorldGenLevel world, BlockPos center, int width, RandomSource random) {
        int peakHeight = SEA_LEVEL + VALLEY_HEIGHT_OFFSET + random.nextInt(MAX_HEIGHT - SEA_LEVEL - VALLEY_HEIGHT_OFFSET);

        for (int x = -width; x <= width; x++) {
            for (int z = -width; z <= width; z++) {
                double distance = Math.sqrt(x * x + z * z) / width;
                int height = calculateHeight(peakHeight, distance, random);

                if (height > SEA_LEVEL) {
                    BlockPos pos = center.offset(x, 0, z);
                    for (int y = SEA_LEVEL; y <= height; y++) {
                        world.setBlock(pos.above(y - SEA_LEVEL), getBlockForHeight(y, height, random), 2);
                    }

                    // Generate cliff entrances
                    if (random.nextFloat() < 0.01 && height > SEA_LEVEL + 50) {
                        generateCliffEntrance(world, pos, height, random);
                    }
                }
            }
        }
        return peakHeight;
    }

    private int calculateHeight(int peakHeight, double distance, RandomSource random) {
        double slopeFactor = 1.0 - distance; // Slope factor decreases with distance
        int adjustedHeight = (int) (peakHeight * slopeFactor); // Adjust height based on distance
        return Math.max(adjustedHeight, SEA_LEVEL); // Ensure height is not below sea level
    }

    private void generateRiver(WorldGenLevel world, BlockPos start, RandomSource random) {
        int riverLength = 500 + random.nextInt(1000);
        BlockPos current = start;

        for (int i = 0; i < riverLength; i++) {
            int dx = random.nextInt(3) - 1;
            int dz = random.nextInt(3) - 1;
            int dy = -1;

            current = current.offset(dx, dy, dz);

            if (world.getBlockState(current).isAir()) {
                continue;
            }

            for (int y = current.getY(); y > SEA_LEVEL; y--) {
                BlockPos waterPos = new BlockPos(current.getX(), y, current.getZ());
                world.setBlock(waterPos, Blocks.WATER.defaultBlockState(), 2);
                
                // Erode surrounding blocks
                for (int ex = -1; ex <= 1; ex++) {
                    for (int ez = -1; ez <= 1; ez++) {
                        BlockPos erodePos = waterPos.offset(ex, 0, ez);
                        if (world.getBlockState(erodePos).is(Blocks.STONE)) {
                            world.setBlock(erodePos, Blocks.GRAVEL.defaultBlockState(), 2);
                        }
                    }
                }
            }

            if (current.getY() <= SEA_LEVEL) {
                break;
            }
        }
    }

    private net.minecraft.world.level.block.state.BlockState getBlockForHeight(int y, int maxHeight, RandomSource random) {
        if (y == maxHeight) {
            return Blocks.GRASS_BLOCK.defaultBlockState();
        } else if (y > maxHeight - 5) {
            return Blocks.DIRT.defaultBlockState();
        } else if (y > maxHeight - 20) {
            return random.nextFloat() < 0.3 ? Blocks.DIRT.defaultBlockState() : Blocks.STONE.defaultBlockState();
        } else {
            return Blocks.STONE.defaultBlockState();
        }
    }

    private void generateCliffEntrance(WorldGenLevel world, BlockPos pos, int height, RandomSource random) {
        int entranceHeight = 3 + random.nextInt(3);
        int entranceWidth = 2 + random.nextInt(3);
        int entranceDepth = 5 + random.nextInt(10);

        for (int y = 0; y < entranceHeight; y++) {
            for (int x = 0; x < entranceWidth; x++) {
                for (int z = 0; z < entranceDepth; z++) {
                    world.setBlock(pos.offset(x, height - SEA_LEVEL - y, z), Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }
    }
}
