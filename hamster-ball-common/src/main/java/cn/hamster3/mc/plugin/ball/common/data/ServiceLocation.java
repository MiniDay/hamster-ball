package cn.hamster3.mc.plugin.ball.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class ServiceLocation {
    private String serverID;
    private String worldName;
    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;

    public int getBlockX() {
        return (int) x;
    }

    public int getBlockY() {
        return (int) y;
    }

    public int getBlockZ() {
        return (int) z;
    }

    @NotNull
    public ServiceBlockPos toServiceBlockPos() {
        return new ServiceBlockPos(getServerID(), getWorldName(), getBlockX(), getBlockY(), getBlockZ());
    }
}
