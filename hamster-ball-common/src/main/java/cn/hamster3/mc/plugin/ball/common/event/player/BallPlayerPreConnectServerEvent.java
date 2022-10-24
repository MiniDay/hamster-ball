package cn.hamster3.mc.plugin.ball.common.event.player;

import cn.hamster3.mc.plugin.ball.common.entity.PlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 玩家准备进入子服
 */
@Data
@AllArgsConstructor
public class BallPlayerPreConnectServerEvent {
    public static final String ACTION = "PlayerPreConnectServer";

    @NotNull
    private final PlayerInfo playerInfo;
    @Nullable
    private final String from;
    @NotNull
    private final String to;

}
