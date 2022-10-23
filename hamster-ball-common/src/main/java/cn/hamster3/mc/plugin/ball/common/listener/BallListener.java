package cn.hamster3.mc.plugin.ball.common.listener;

import cn.hamster3.mc.plugin.ball.common.data.ServiceMessageInfo;
import cn.hamster3.mc.plugin.ball.common.event.operate.*;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOfflineEvent;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BallListener {
    /**
     * 该监听器的执行优先级
     * <p>
     * 数字越低越先执行
     *
     * @return 优先级
     */
    public int getPriority() {
        return 100;
    }

    public void onBroadcastPlayerMessage(@NotNull BroadcastPlayerMessageEvent event) {
    }

    public void onDispatchConsoleCommand(@NotNull DispatchConsoleCommandEvent event) {
    }

    public void onDispatchGamePlayerCommand(@NotNull DispatchPlayerCommandEvent event) {
    }

    public void onKickPlayer(@NotNull KickPlayerEvent event) {
    }

    public void onSendMessageToPlayer(@NotNull SendMessageToPlayerEvent event) {
    }

    public void onSendPlayerToLocation(@NotNull SendPlayerToLocationEvent event) {
    }

    public void onSendPlayerToPlayer(@NotNull SendPlayerToPlayerEvent event) {
    }

    public void onPlayerChat(@NotNull PlayerChatEvent event) {
    }

    public void onPlayerConnectServer(@NotNull PlayerConnectServerEvent event) {
    }

    public void onPlayerDisconnect(@NotNull PlayerDisconnectEvent event) {
    }

    public void onPlayerLogin(@NotNull PlayerLoginEvent event) {
    }

    public void onPlayerPostConnectServer(@NotNull PlayerPostConnectServerEvent event) {
    }

    public void onPlayerPostLogin(@NotNull PlayerPostLoginEvent event) {
    }

    public void onPlayerPreConnectServer(@NotNull PlayerPreConnectServerEvent event) {
    }

    public void onPlayerPreLogin(@NotNull PlayerPreLoginEvent event) {
    }

    public void onServerOffline(@NotNull ServerOfflineEvent event) {
    }

    public void onServerOnline(@NotNull ServerOnlineEvent event) {
    }

    public void onMessageReceived(@NotNull ServiceMessageInfo event) {
    }

    public void onMessageSend(@NotNull ServiceMessageInfo event) {
    }

    public void onConnectInactive() {
    }

    public void onConnectException(Throwable throwable) {
    }

    public void onReconnectFailed() {
    }

}
