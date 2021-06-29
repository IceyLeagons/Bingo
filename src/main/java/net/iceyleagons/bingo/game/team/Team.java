package net.iceyleagons.bingo.game.team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.items.ItemDictionary;
import net.iceyleagons.bingo.items.MapImage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Team {

    private final Game game;
    private final int id;
    private final int playersPerTeam;
    private final Location spawnPoint;
    private final TeamProgressHandler teamProgressHandler = new TeamProgressHandler();
    private MapImage mapImage;

    private final Set<Player> members = new HashSet<>();

    public void onGameStarted(ItemDictionary chosenItems) {
        mapImage = new MapImage(chosenItems, this);
        members.forEach(m -> m.teleport(spawnPoint));
    }

    /**
     * Broadcasts a message for this team's members as a "team" message.
     *
     * @param message the message from {@link AsyncPlayerChatEvent#getMessage()}
     * @param player the player who sent the message
     */
    public void onPlayerMessage(String message, Player player) {
        members.forEach(m -> {
            String msg = String.format("&8[&aTeam&8] &7%s&8: &f", player.getName());

            //message is appended separately because we don't want to parse colors there.
            m.sendMessage(ChatColor.translateAlternateColorCodes('&', msg) + message);
        });
    }

    /**
     * Joins the player to this team.
     * This will send out broadcasts and add the player to the {@link Game#getPlayers()} map as well.
     *
     * @param player the {@link Player}
     */
    public void joinPlayer(Player player) {
        game.getPlayers().put(player, this);
        members.add(player);

        game.broadcast(String.format("&8[&a+&8] &e%s &9has joined the game.", player.getName()));
    }
}
