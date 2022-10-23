package cn.hamster3.mc.plugin.ball.common.config;

import cn.hamster3.mc.plugin.ball.common.entity.ServerInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class BallConfig {
    @NotNull
    private ServerInfo localInfo;

    @NotNull
    private String host;
    private int port;

    private int nioThread;
}
