package cn.hamster3.mc.plugin.ball.common.event.server;

import cn.hamster3.mc.plugin.ball.common.entity.ServerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 服务器上线
 */
@Data
@AllArgsConstructor
public class ServerOnlineEvent {
    public static final String ACTION = "ServerOnline";

    @NotNull
    private final ServerInfo serverInfo;

}
