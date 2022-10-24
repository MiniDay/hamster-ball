package cn.hamster3.mc.plugin.ball.common.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 玩家的聊天信息
 */
@Data
@AllArgsConstructor
public class BallPlayerChatEvent {
    public static final String ACTION = "PlayerChat";

    @NotNull
    private final UUID playerUUID;
    @NotNull
    private final Component displayName;
    @NotNull
    private final Component message;

}
