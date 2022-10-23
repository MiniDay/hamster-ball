package cn.hamster3.mc.plugin.ball.bukkit.data;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.ServiceBlockPos;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BukkitBlockPos extends ServiceBlockPos {
    public BukkitBlockPos(@NotNull String serverID, @NotNull String worldName, int x, int y, int z) {
        super(serverID, worldName, x, y, z);
    }

    public BukkitBlockPos(@NotNull Entity player) {
        this(player.getLocation());
    }

    public BukkitBlockPos(@NotNull Block block) {
        this(block.getLocation());
    }

    public BukkitBlockPos(@NotNull Location location) {
        super(
                BallAPI.getInstance().getLocalServerId(),
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    public BukkitBlockPos(@NotNull ServiceBlockPos location) {
        super(
                BallAPI.getInstance().getLocalServerId(),
                location.getWorldName(),
                location.getX(),
                location.getY(),
                location.getZ()
        );
    }

    @NotNull
    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(getWorldName()), getX(), getY(), getZ(), 0, 0);
    }

    @NotNull
    public BukkitLocation toServiceLocation() {
        return new BukkitLocation(getServerID(), getWorldName(), getX(), getY(), getZ());
    }
}
