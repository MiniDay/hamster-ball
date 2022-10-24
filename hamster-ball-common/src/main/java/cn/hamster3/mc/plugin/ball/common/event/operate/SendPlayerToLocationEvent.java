package cn.hamster3.mc.plugin.ball.common.event.operate;

import cn.hamster3.mc.plugin.ball.common.data.ServiceLocation;
import cn.hamster3.mc.plugin.core.common.data.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SendPlayerToLocationEvent {
    public static final String ACTION = "SendPlayerToLocation";

    @NotNull
    private final Set<UUID> sendPlayerUUID;
    @NotNull
    private final ServiceLocation location;
    @Nullable
    private final Message doneMessage;

}
