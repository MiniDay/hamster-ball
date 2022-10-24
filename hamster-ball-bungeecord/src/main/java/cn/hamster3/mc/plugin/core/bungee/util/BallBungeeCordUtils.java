package cn.hamster3.mc.plugin.core.bungee.util;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.entity.PlayerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public final class BallBungeeCordUtils {
    private BallBungeeCordUtils() {
    }

    public static PlayerInfo getPlayerInfo(ProxiedPlayer player, boolean online) {
        Server server = player.getServer();
        return new PlayerInfo(
                player.getUniqueId(),
                player.getName(),
                server == null ? "" : server.getInfo().getName(),
                BallAPI.getInstance().getLocalServerId(),
                online
        );
    }
}
