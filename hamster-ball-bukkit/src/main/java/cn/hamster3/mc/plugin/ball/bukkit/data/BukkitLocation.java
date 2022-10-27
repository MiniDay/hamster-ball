package cn.hamster3.mc.plugin.ball.bukkit.data;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.BallLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BukkitLocation extends BallLocation {
    public BukkitLocation(@NotNull String serverID, @NotNull String worldName, double x, double y, double z) {
        super(serverID, worldName, x, y, z, 0, 0);
    }

    public BukkitLocation(@NotNull String serverID, @NotNull String worldName, double x, double y, double z, float yaw, float pitch) {
        super(serverID, worldName, x, y, z, yaw, pitch);
    }

    public BukkitLocation(@NotNull Entity player) {
        this(player.getLocation());
    }

    public BukkitLocation(@NotNull Block block) {
        this(block.getLocation());
    }

    public BukkitLocation(@NotNull Location location) {
        super(
                BallAPI.getInstance().getLocalServerId(),
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public BukkitLocation(@NotNull BallLocation location) {
        super(
                location.getServerID(),
                location.getWorldName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    @NotNull
    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(getWorldName()), getX(), getY(), getZ(), getYaw(), getPitch());
    }

    @NotNull
    public BukkitBlockPos toBukkitBlockPos() {
        return new BukkitBlockPos(getServerID(), getWorldName(), getBlockX(), getBlockY(), getBlockZ());
    }
}
