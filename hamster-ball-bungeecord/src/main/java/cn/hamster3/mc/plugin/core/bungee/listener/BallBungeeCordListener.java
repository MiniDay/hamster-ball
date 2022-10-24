package cn.hamster3.mc.plugin.core.bungee.listener;

import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.entity.PlayerInfo;
import cn.hamster3.mc.plugin.ball.common.entity.ServerType;
import cn.hamster3.mc.plugin.ball.common.event.operate.*;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import cn.hamster3.mc.plugin.core.bungee.util.BallBungeeCordUtils;
import cn.hamster3.mc.plugin.core.common.api.CoreAPI;
import cn.hamster3.mc.plugin.core.common.data.Message;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class BallBungeeCordListener extends BallListener implements Listener {
    public static final BallBungeeCordListener INSTANCE = new BallBungeeCordListener();

    private BallBungeeCordListener() {
    }

    @Override
    public void onBroadcastPlayerMessage(@NotNull BroadcastPlayerMessageEvent event) {
        Message message = event.getMessage();
        Audience audience = CoreAPI.getInstance().getAudienceProvider().all();
        message.show(audience);
    }

    @Override
    public void onDispatchConsoleCommand(@NotNull DispatchConsoleCommandEvent event) {
        if (event.getType() != null && event.getType() != ServerType.PROXY) {
            return;
        }
        if (event.getServerID() != null && !BallAPI.getInstance().isLocalServer(event.getServerID())) {
            return;
        }
        ProxyServer server = ProxyServer.getInstance();
        server.getPluginManager().dispatchCommand(server.getConsole(), event.getCommand());
    }

    @Override
    public void onDispatchPlayerCommand(@NotNull DispatchPlayerCommandEvent event) {
        if (event.getType() != null && event.getType() != ServerType.GAME) {
            return;
        }
        ProxyServer server = ProxyServer.getInstance();
        if (event.getUuid() != null) {
            ProxiedPlayer player = server.getPlayer(event.getUuid());
            if (player == null) {
                return;
            }
            server.getPluginManager().dispatchCommand(player, event.getCommand());
            return;
        }
        for (ProxiedPlayer player : server.getPlayers()) {
            server.getPluginManager().dispatchCommand(player, event.getCommand());
        }
    }

    @Override
    public void onSendMessageToPlayer(@NotNull SendMessageToPlayerEvent event) {
        for (UUID uuid : event.getReceiver()) {
            Audience audience = CoreAPI.getInstance().getAudienceProvider().player(uuid);
            event.getMessage().show(audience);
        }
    }

    @Override
    public void onKickPlayer(@NotNull KickPlayerEvent event) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getUuid());
        BaseComponent[] components = BungeeComponentSerializer.get().serialize(event.getReason());
        player.disconnect(components);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(PreLoginEvent event) {
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerPreLoginEvent.ACTION,
                new BallPlayerPreLoginEvent(event.getConnection().getName())
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(LoginEvent event) {
        if (event.isCancelled()) {
            return;
        }
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerLoginEvent.ACTION,
                new BallPlayerLoginEvent(new PlayerInfo(
                        event.getConnection().getUniqueId(),
                        event.getConnection().getName(),
                        "",
                        BallAPI.getInstance().getLocalServerId(),
                        true
                ))
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerPostLoginEvent.ACTION,
                new BallPlayerPostLoginEvent(BallBungeeCordUtils.getPlayerInfo(player, true))
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PlayerInfo playerInfo = BallBungeeCordUtils.getPlayerInfo(player, true);
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerConnectServerEvent.ACTION,
                new BallPlayerConnectServerEvent(playerInfo, playerInfo.getGameServer(), event.getTarget().getName())
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PlayerInfo playerInfo = BallBungeeCordUtils.getPlayerInfo(player, true);
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerPostConnectServerEvent.ACTION,
                new BallPlayerPostConnectServerEvent(playerInfo)
        );
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onServerDisconnect(ServerDisconnectEvent event) {
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        BallAPI.getInstance().sendBallMessage(
                BallAPI.BALL_CHANNEL,
                BallPlayerLogoutEvent.ACTION,
                new BallPlayerLogoutEvent(BallBungeeCordUtils.getPlayerInfo(player, false))
        );
    }
}
