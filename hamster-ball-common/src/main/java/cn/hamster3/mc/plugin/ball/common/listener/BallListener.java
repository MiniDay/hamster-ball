package cn.hamster3.mc.plugin.ball.common.listener;

import cn.hamster3.mc.plugin.ball.common.data.MessageInfo;
import cn.hamster3.mc.plugin.ball.common.event.operate.*;
import cn.hamster3.mc.plugin.ball.common.event.player.*;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOfflineEvent;
import cn.hamster3.mc.plugin.ball.common.event.server.ServerOnlineEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BallListener {
    /**
     * 该监听器的执行优先级
     *
     * @return 优先级
     */
    public ListenerPriority getPriority() {
        return ListenerPriority.NORMAL;
    }

    public void onBroadcastPlayerMessage(@NotNull BroadcastPlayerMessageEvent event) {
    }

    public void onDispatchConsoleCommand(@NotNull DispatchConsoleCommandEvent event) {
    }

    public void onDispatchPlayerCommand(@NotNull DispatchPlayerCommandEvent event) {
    }

    public void onKickPlayer(@NotNull KickPlayerEvent event) {
    }

    public void onSendMessageToPlayer(@NotNull SendMessageToPlayerEvent event) {
    }

    public void onSendPlayerToLocation(@NotNull SendPlayerToLocationEvent event) {
    }

    public void onSendPlayerToPlayer(@NotNull SendPlayerToPlayerEvent event) {
    }

    public void onBallPlayerPreLogin(@NotNull BallPlayerPreLoginEvent event) {
    }

    public void onBallPlayerLogin(@NotNull BallPlayerLoginEvent event) {
    }

    public void onBallPlayerPostLogin(@NotNull BallPlayerPostLoginEvent event) {
    }

    public void onBallPlayerPreConnectServer(@NotNull BallPlayerPreConnectServerEvent event) {
    }

    public void onBallPlayerConnectServer(@NotNull BallPlayerConnectServerEvent event) {
    }

    public void onBallPlayerPostConnectServer(@NotNull BallPlayerPostConnectServerEvent event) {
    }

    public void onBallPlayerLogout(@NotNull BallPlayerLogoutEvent event) {
    }

    public void onBallPlayerChat(@NotNull BallPlayerChatEvent event) {
    }

    public void onServerOffline(@NotNull ServerOfflineEvent event) {
    }

    public void onServerOnline(@NotNull ServerOnlineEvent event) {
    }

    public void onMessageSend(@NotNull MessageInfo event) {
    }

    public void onMessageReceived(@NotNull MessageInfo event) {
    }

    public void onConnectInactive() {
    }

    public void onConnectException(Throwable throwable) {
    }

    public void onReconnectFailed() {
    }

}
