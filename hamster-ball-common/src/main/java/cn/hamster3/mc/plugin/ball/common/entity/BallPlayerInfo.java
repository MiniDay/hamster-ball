package cn.hamster3.mc.plugin.ball.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 玩家信息
 */
@Data
@NotNull
@AllArgsConstructor
public class BallPlayerInfo {
    /**
     * 玩家的uuid
     */
    @NotNull
    private UUID uuid;
    /**
     * 玩家的名称
     */
    @NotNull
    private String name;
    /**
     * 玩家所在的游戏服务器 ID
     * <p>
     * 不应超过 32 个字符
     */
    @NotNull
    private String gameServer;
    /**
     * 玩家所在的代理服务器 ID
     * <p>
     * 不应超过 32 个字符
     */
    @NotNull
    private String proxyServer;
    /**
     * 玩家是否在线
     */
    private boolean online;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BallPlayerInfo that = (BallPlayerInfo) o;

        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
