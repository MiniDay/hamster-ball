package cn.hamster3.mc.plugin.ball.bukkit.api;

import cn.hamster3.mc.plugin.ball.bukkit.HamsterBallPlugin;
import cn.hamster3.mc.plugin.ball.bukkit.listener.BallBukkitListener;
import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.config.BallConfig;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import cn.hamster3.mc.plugin.ball.common.listener.BallDebugListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.logging.Logger;

public class BallBukkitAPI extends BallAPI {
    public BallBukkitAPI(@NotNull BallConfig config) {
        super(config);
    }

    public static BallBukkitAPI getInstance() {
        return (BallBukkitAPI) instance;
    }

    public static void init() {
        if (instance != null) {
            return;
        }
        HamsterBallPlugin plugin = HamsterBallPlugin.getInstance();
        plugin.saveDefaultConfig();
        FileConfiguration pluginConfig = plugin.getConfig();

        String host = pluginConfig.getString("server-info.name.host", Bukkit.getIp());
        BallConfig config = new BallConfig(
                new BallServerInfo(
                        pluginConfig.getString("server-info.id"),
                        pluginConfig.getString("server-info.name"),
                        BallServerType.GAME,
                        host.isEmpty() ? "127.0.0.1" : host,
                        pluginConfig.getInt("server-info.name.port", Bukkit.getPort())
                ),
                pluginConfig.getString("ball-server.host"),
                pluginConfig.getInt("ball-server.port"),
                pluginConfig.getInt("ball-server.nio-thread")
        );
        instance = new BallBukkitAPI(config);

        instance.addListener(BallBukkitListener.INSTANCE);
        if (pluginConfig.getBoolean("debug", false)) {
            instance.addListener(BallDebugListener.INSTANCE);
        }
    }

    @Override
    public void enable() throws SQLException, InterruptedException {
        super.enable();
    }

    @Override
    public void disable() throws SQLException, InterruptedException {
        super.disable();
    }

    @Override
    public @NotNull Logger getLogger() {
        return HamsterBallPlugin.getInstance().getLogger();
    }
}
