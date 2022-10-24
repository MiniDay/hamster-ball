package cn.hamster3.mc.plugin.ball.common.event.player;

import cn.hamster3.mc.plugin.ball.common.entity.PlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家连接到代理服务器
 */
@Data
@AllArgsConstructor
public class BallPlayerLoginEvent {
    public static final String ACTION = "PlayerLogin";

    @NotNull
    private final PlayerInfo playerInfo;

}
