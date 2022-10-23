package cn.hamster3.mc.plugin.ball.common.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 玩家与服务器断开连接
 */
@Data
@AllArgsConstructor
public class PlayerDisconnectEvent {
    public static final String ACTION = "PlayerDisconnect";

    @NotNull
    private final UUID playerUUID;
    private final String serverID;
}
