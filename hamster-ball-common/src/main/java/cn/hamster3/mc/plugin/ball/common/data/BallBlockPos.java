package cn.hamster3.mc.plugin.ball.common.data;

import cn.hamster3.mc.plugin.core.common.constant.CoreConstantObjects;
import com.google.gson.JsonElement;
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

    public static BallBlockPos fromJson(String json) {
        return CoreConstantObjects.GSON.fromJson(json, BallBlockPos.class);
    }

    public JsonElement toJson() {
        return CoreConstantObjects.GSON.toJsonTree(this);
    }

    @NotNull
    public BallLocation toServiceLocation() {
        return new BallLocation(getServerID(), getWorldName(), getX(), getY(), getZ(), 0, 0);
    }
}
