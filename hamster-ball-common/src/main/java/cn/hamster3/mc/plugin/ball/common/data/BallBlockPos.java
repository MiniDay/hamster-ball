package cn.hamster3.mc.plugin.ball.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class BallBlockPos {
    private String serverID;
    private String worldName;
    private int x;
    private int y;
    private int z;

    @NotNull
    public BallLocation toServiceLocation() {
        return new BallLocation(getServerID(), getWorldName(), getX(), getY(), getZ(), 0, 0);
    }
}
