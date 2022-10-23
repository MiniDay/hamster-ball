package cn.hamster3.mc.plugin.ball.common.event.operate;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
public class KickPlayerEvent {
    public static final String ACTION = "KickPlayer";

    @NotNull
    private final UUID uuid;
    @NotNull
    private final Component reason;

}
