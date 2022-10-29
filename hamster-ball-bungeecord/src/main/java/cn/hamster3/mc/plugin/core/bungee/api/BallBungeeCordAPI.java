package cn.hamster3.mc.plugin.core.bungee.api;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.config.BallConfig;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import cn.hamster3.mc.plugin.ball.common.listener.BallDebugListener;
import cn.hamster3.mc.plugin.core.bungee.HamsterBallPlugin;
import cn.hamster3.mc.plugin.core.bungee.listener.BallBungeeCordListener;
import cn.hamster3.mc.plugin.core.bungee.util.BungeeCordUtils;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.logging.Logger;

public class BallBungeeCordAPI extends BallAPI {
    public BallBungeeCordAPI(@NotNull BallConfig config) {
        super(config);
    }

    public static BallBungeeCordAPI getInstance() {
        return (BallBungeeCordAPI) instance;
    }

    public static void init() {
        if (instance != null) {
            return;
        }
        HamsterBallPlugin plugin = HamsterBallPlugin.getInstance();
        Configuration pluginConfig = BungeeCordUtils.getPluginConfig(plugin);
        BallConfig config = new BallConfig(
                new BallServerInfo(
                        pluginConfig.getString("server-info.id"),
                        pluginConfig.getString("server-info.name"),
                        BallServerType.PROXY,
                        pluginConfig.getString("server-info.host", ""),
                        pluginConfig.getInt("server-info.port", 25577)
                ),
                pluginConfig.getString("ball-server.host"),
                pluginConfig.getInt("ball-server.port"),
                pluginConfig.getInt("ball-server.nio-thread")
        );
        instance = new BallBungeeCordAPI(config);

        instance.addListener(BallBungeeCordListener.INSTANCE);
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
