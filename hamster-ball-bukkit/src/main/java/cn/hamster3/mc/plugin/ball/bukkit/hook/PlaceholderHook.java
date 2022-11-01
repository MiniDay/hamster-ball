package cn.hamster3.mc.plugin.ball.bukkit.hook;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {
    public static final PlaceholderHook INSTANCE = new PlaceholderHook();

    private PlaceholderHook() {
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hamster_ball";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MiniDay";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        switch (params) {
            case "local_server_id": {
                return BallAPI.getInstance().getLocalServerInfo().getId();
            }
            case "local_server_name": {
                return BallAPI.getInstance().getLocalServerInfo().getName();
            }
            case "proxy_id": {
                return BallAPI.getInstance().getPlayerInfo(player.getUniqueId()).getProxyServer();
            }
            case "proxy_name": {
                String id = BallAPI.getInstance().getPlayerInfo(player.getUniqueId()).getProxyServer();
                return BallAPI.getInstance().getServerInfo(id).getName();
            }
        }
        return null;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return onRequest(player, params);
    }
}
