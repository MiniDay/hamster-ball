package cn.hamster3.mc.plugin.ball.common.event.player;

import cn.hamster3.mc.plugin.ball.common.entity.BallPlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家已连接到代理服务器
 */
@Data
@AllArgsConstructor
public class BallPlayerPostLoginEvent {
    public static final String ACTION = "PlayerPostLogin";

    @NotNull
    private final BallPlayerInfo playerInfo;
}
