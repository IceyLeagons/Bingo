package net.iceyleagons.bingo.game;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class GameListener implements Listener {

    private final Game game;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent playerChatEvent) {
        if (!game.getPlayers().containsKey(playerChatEvent.getPlayer())) return;
        playerChatEvent.setCancelled(true);

        if (playerChatEvent.getMessage().startsWith("!")) {
            game.broadcastMessage(playerChatEvent.getMessage().replaceFirst("!", ""), playerChatEvent.getPlayer());
            return;
        }

        game.getPlayers().get(playerChatEvent.getPlayer()).onPlayerMessage(playerChatEvent.getMessage(), playerChatEvent.getPlayer());
    }
}
