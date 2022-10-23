package cn.hamster3.mc.plugin.ball.common.event.operate;

import cn.hamster3.mc.plugin.core.common.data.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SendMessageToPlayerEvent {
    public static final String ACTION = "SendMessageToPlayer";

    @NotNull
    private final UUID uuid;
    @NotNull
    private final Message message;
}
