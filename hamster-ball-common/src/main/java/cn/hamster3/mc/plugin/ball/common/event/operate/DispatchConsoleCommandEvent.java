package cn.hamster3.mc.plugin.ball.common.event.operate;

import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class DispatchConsoleCommandEvent {
    public static final String ACTION = "DispatchConsoleCommand";

    @Nullable
    private final BallServerType type;
    @Nullable
    private final String serverID;
    @NotNull
    private final String command;
}
