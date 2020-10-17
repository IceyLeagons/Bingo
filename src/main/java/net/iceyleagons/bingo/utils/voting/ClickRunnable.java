package net.iceyleagons.bingo.utils.voting;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author TOTHTOMI
 */
public abstract class ClickRunnable {
    public abstract void run(InventoryClickEvent event, Player player);
}