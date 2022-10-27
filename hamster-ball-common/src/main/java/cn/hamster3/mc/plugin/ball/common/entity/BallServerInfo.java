package cn.hamster3.mc.plugin.ball.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 消息发送者信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BallServerInfo {
    /**
     * 服务器 ID
     * <p>
     * 不应超过 32 个字符
     */
    private String id;
    /**
     * 服务器名称
     * <p>
     * 不应超过 32 个字符
     */
    private String name;
    /**
     * 服务器类型
     */
    private BallServerType type;
    /**
     * 服务器主机名
     * <p>
     * 不应超过 32 个字符
     */
    private String host;
    /**
     * 服务器端口号
     */
    private int port;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BallServerInfo that = (BallServerInfo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
