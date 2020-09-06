package external.protocol_glowapi.listeners;

import external.protocol_glowapi.GlowAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        GlowAPI.initTeams(event.getPlayer());
    }

}