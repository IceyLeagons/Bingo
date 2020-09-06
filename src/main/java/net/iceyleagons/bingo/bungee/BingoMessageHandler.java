package net.iceyleagons.bingo.bungee;

import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public interface BingoMessageHandler {

    void handle(String message, Player player);
    String getSubchannel();
}
