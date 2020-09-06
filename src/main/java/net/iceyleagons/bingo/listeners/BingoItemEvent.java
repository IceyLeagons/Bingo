package net.iceyleagons.bingo.listeners;

import lombok.Data;
import lombok.Getter;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.game.teams.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called every single time a player picks up/crafts an item, also when they find it end take it from a chest.
 *
 * @author TOTHTOMI
 */
public class BingoItemEvent extends Event {


    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Getter
    private final ItemStack itemStack;
    @Getter
    private final Player player;
    @Getter
    private final Team team;
    @Getter
    private final Game game;

    public BingoItemEvent(ItemStack itemStack, Player player, Team team, Game game) {
        this.itemStack = itemStack;
        this.player = player;
        this.team = team;
        this.game = game;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
