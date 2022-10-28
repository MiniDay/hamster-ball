package cn.hamster3.mc.plugin.ball.common.event.operate;

import cn.hamster3.mc.plugin.core.common.data.DisplayMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class BroadcastPlayerMessageEvent {
    public static final String ACTION = "BroadcastPlayerMessage";

    @NotNull
    private final DisplayMessage message;

}
