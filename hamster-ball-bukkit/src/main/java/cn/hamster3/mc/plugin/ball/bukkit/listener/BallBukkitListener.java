package cn.hamster3.mc.plugin.ball.bukkit.listener;

import cn.hamster3.mc.plugin.ball.bukkit.data.BukkitLocation;
import cn.hamster3.mc.plugin.ball.common.api.BallAPI;
import cn.hamster3.mc.plugin.ball.common.data.BallLocation;
import cn.hamster3.mc.plugin.ball.common.data.BallMessageInfo;
import cn.hamster3.mc.plugin.ball.common.entity.BallServerType;
import cn.hamster3.mc.plugin.ball.common.event.operate.DispatchConsoleCommandEvent;
import cn.hamster3.mc.plugin.ball.common.event.operate.DispatchPlayerCommandEvent;
import cn.hamster3.mc.plugin.ball.common.event.operate.SendPlayerToLocationEvent;
import cn.hamster3.mc.plugin.ball.common.event.operate.SendPlayerToPlayerEvent;
import cn.hamster3.mc.plugin.ball.common.listener.BallListener;
import cn.hamster3.mc.plugin.core.common.constant.CoreConstantObjects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    private BallBukkitListener() {
    }

    @Override
    public void onServiceDead() {
        Bukkit.shutdown();
    }

    @Override
    public void onMessageReceived(@NotNull BallMessageInfo info) {
        switch (info.getAction()) {
            case DispatchConsoleCommandEvent.ACTION: {
                DispatchConsoleCommandEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), DispatchConsoleCommandEvent.class);
                if (event.getType() != null && event.getType() != BallServerType.GAME) {
                    return;
                }
                if (event.getServerID() != null && !BallAPI.getInstance().isLocalServer(event.getServerID())) {
                    return;
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand());
                break;
            }
            case DispatchPlayerCommandEvent.ACTION: {
                DispatchPlayerCommandEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), DispatchPlayerCommandEvent.class);
                if (event.getType() != null && event.getType() != BallServerType.GAME) {
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
                break;
            }
            case SendPlayerToLocationEvent.ACTION: {
                SendPlayerToLocationEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), SendPlayerToLocationEvent.class);
                BallLocation location = event.getLocation();
                if (BallAPI.getInstance().isLocalServer(location.getServerID())) {
                    for (UUID uuid : event.getSendPlayerUUID()) {
                        playerToLocation.put(uuid, new BukkitLocation(location).toBukkitLocation());
                    }
                }
                break;
            }
            case SendPlayerToPlayerEvent.ACTION: {
                SendPlayerToPlayerEvent event = CoreConstantObjects.GSON.fromJson(info.getContent(), SendPlayerToPlayerEvent.class);
                Player player = Bukkit.getPlayer(event.getToPlayerUUID());
                if (player == null) {
                    return;
                }
                Location location = player.getLocation();
                for (UUID uuid : event.getSendPlayerUUID()) {
                    playerToLocation.put(uuid, location);
                }
                break;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Location location = playerToLocation.remove(player.getUniqueId());
        if (location != null) {
            player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

    }
}
