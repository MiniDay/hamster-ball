package cn.hamster3.mc.plugin.ball.common.event.player;

import cn.hamster3.mc.plugin.ball.common.entity.PlayerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家已连接到服务器
 */
@Data
@AllArgsConstructor
public class PlayerPostLoginEvent {
    public static final String ACTION = "PlayerPostLogin";

    @NotNull
    private final PlayerInfo playerInfo;
}
