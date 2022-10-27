package cn.hamster3.mc.plugin.ball.common.event.player;

import cn.hamster3.mc.plugin.ball.common.entity.BallPlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 玩家准备进入子服
 * <p>
 * 仅在使用 velocity 代理端时才会触发这个事件
 */
@Data
@AllArgsConstructor
public class BallPlayerPreConnectServerEvent {
    public static final String ACTION = "PlayerPreConnectServer";

    @NotNull
    private final BallPlayerInfo playerInfo;
    @Nullable
    private final String from;
    @NotNull
    private final String to;

}
