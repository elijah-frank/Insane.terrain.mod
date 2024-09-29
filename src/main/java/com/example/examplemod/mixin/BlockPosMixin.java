package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.example.examplemod.Constants;

import net.minecraft.core.BlockPos;

@Mixin(BlockPos.class)
public class BlockPosMixin {

    // Redirect the clamp method in the constructor to allow extended Y levels
    @Redirect(method = "<init>(III)V", at = @At(value = "INVOKE", target = "Ljava/lang/Math;clamp(III)I"))
    private int redirectClamp(int value, int min, int max) {
        int newMin = Constants.MIN_HEIGHT;
        int newMax = Constants.MAX_HEIGHT;
        return Math.clamp(value, newMin, newMax);
    }

    // Repeat similar redirects for other constructors or methods enforcing Y limits
}