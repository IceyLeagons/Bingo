package net.iceyleagons.bingo.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.bingo.game.team.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class BingoItemEvent extends Event {
    private final static HandlerList handlerList = new HandlerList();

    private final Player player;
    private final Team team;
    private final Material material;


    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
