package com.example.examplemod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerSpawnHandler {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        ServerLevel world = (ServerLevel) player.level();

        // Set player spawn position to mid-height
        BlockPos spawnPos = new BlockPos(0, Constants.MAX_HEIGHT / 2, 0);
        player.teleportTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        world.setSpawnPos(spawnPos, 0.0F);
    }
}