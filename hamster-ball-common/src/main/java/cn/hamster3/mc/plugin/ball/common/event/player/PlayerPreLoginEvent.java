package cn.hamster3.mc.plugin.ball.common.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 玩家准备连接到服务器
 */
@Data
@AllArgsConstructor
public class PlayerPreLoginEvent {
    public static final String ACTION = "PlayerPreLogin";

    @NotNull
    private final String playerName;
}
