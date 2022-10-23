package cn.hamster3.mc.plugin.ball.common.event.operate;

import cn.hamster3.mc.plugin.core.common.data.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SendPlayerToPlayerEvent {
    public static final String ACTION = "SendPlayerToPlayer";

    @NotNull
    private final Set<UUID> sendPlayerUUID;
    @NotNull
    private final UUID toPlayerUUID;
    @Nullable
    private final Message doneMessage;
    @Nullable
    private final Message doneTargetMessage;

}
