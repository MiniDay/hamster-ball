package cn.hamster3.mc.plugin.ball.common.event.player;

import cn.hamster3.mc.plugin.ball.common.entity.BallPlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家已进入子服
 */
@Data
@AllArgsConstructor
public class BallPlayerPostConnectServerEvent {
    public static final String ACTION = "PlayerPostConnectServer";

    @NotNull
    private final BallPlayerInfo playerInfo;
}
