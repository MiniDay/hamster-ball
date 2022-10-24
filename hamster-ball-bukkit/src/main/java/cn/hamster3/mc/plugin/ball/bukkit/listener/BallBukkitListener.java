package cn.hamster3.mc.plugin.ball.bukkit.listener;

import cn.hamster3.mc.plugin.ball.bukkit.data.BukkitLocation;
import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.ServiceLocation;
import cn.hamster3.mc.plugin.ball.common.entity.ServerType;
import cn.hamster3.mc.plugin.ball.common.event.operate.DispatchConsoleCommandEvent;
import cn.hamster3.mc.plugin.ball.common.event.operate.DispatchPlayerCommandEvent;
import cn.hamster3.mc.plugin.ball.common.event.operate.SendPlayerToLocationEvent;
import cn.hamster3.mc.plugin.ball.common.event.operate.SendPlayerToPlayerEvent;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class BallBukkitListener extends BallListener implements Listener {
    public static final BallBukkitListener INSTANCE = new BallBukkitListener();

    private final HashMap<UUID, Location> playerToLocation = new HashMap<>();

    @Override
    public void onDispatchConsoleCommand(@NotNull DispatchConsoleCommandEvent event) {
        if (event.getType() != null && event.getType() != ServerType.GAME) {
            return;
        }
        if (event.getServerID() != null && !BallAPI.getInstance().isLocalServer(event.getServerID())) {
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand());
    }

    @Override
    public void onDispatchPlayerCommand(@NotNull DispatchPlayerCommandEvent event) {
        if (event.getType() != null && event.getType() != ServerType.GAME) {
            return;
        }
        if (event.getUuid() != null) {
            Player player = Bukkit.getPlayer(event.getUuid());
            if (player == null) {
                return;
            }
            Bukkit.dispatchCommand(player, event.getCommand());
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.dispatchCommand(player, event.getCommand());
        }
    }

    @Override
    public void onSendPlayerToLocation(@NotNull SendPlayerToLocationEvent event) {
        ServiceLocation location = event.getLocation();
        if (!BallAPI.getInstance().isLocalServer(location.getServerID())) {
            return;
        }
        for (UUID uuid : event.getSendPlayerUUID()) {
            playerToLocation.put(uuid, new BukkitLocation(location).toBukkitLocation());
        }
    }

    @Override
    public void onSendPlayerToPlayer(@NotNull SendPlayerToPlayerEvent event) {
        Player player = Bukkit.getPlayer(event.getToPlayerUUID());
        if (player == null) {
            return;
        }
        Location location = player.getLocation();
        for (UUID uuid : event.getSendPlayerUUID()) {
            playerToLocation.put(uuid, location);
        }
    }

    @Override
    public void onReconnectFailed() {
        Bukkit.shutdown();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Location location = playerToLocation.remove(player.getUniqueId());
        if (location != null) {
            player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

    }
}
