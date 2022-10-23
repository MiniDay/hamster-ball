package cn.hamster3.mc.plugin.ball.common.event.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 服务器离线
 */
@Data
@AllArgsConstructor
public class ServerOfflineEvent {
    public static final String ACTION = "ServerOffline";

    @NotNull
    private final String serverID;
}
