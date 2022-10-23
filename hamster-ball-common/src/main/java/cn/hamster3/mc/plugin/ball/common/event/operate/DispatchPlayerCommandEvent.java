package cn.hamster3.mc.plugin.ball.common.event.operate;

import cn.hamster3.mc.plugin.ball.common.entity.ServerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DispatchPlayerCommandEvent {
    public static final String ACTION = "DispatchPlayerCommand";

    @Nullable
    private final ServerType type;
    @Nullable
    private final UUID uuid;
    @NotNull
    private final String command;
}
