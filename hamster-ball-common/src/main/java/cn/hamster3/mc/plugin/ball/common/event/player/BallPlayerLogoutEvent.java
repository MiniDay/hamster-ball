package cn.hamster3.mc.plugin.ball.common.event.player;

import cn.hamster3.mc.plugin.ball.common.entity.BallPlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家与代理服务器断开连接
 */
@Data
@AllArgsConstructor
public class BallPlayerLogoutEvent {
    public static final String ACTION = "PlayerLogout";

    @NotNull
    private BallPlayerInfo playerInfo;
}
