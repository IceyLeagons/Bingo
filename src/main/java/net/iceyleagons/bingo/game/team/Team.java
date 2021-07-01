package net.iceyleagons.bingo.game.team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.items.BingoRenderer;
import net.iceyleagons.bingo.items.ItemDictionary;
import net.iceyleagons.bingo.items.MapImage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.HashSet;
import java.util.Objects;
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
    private BingoRenderer bingoRenderer;
    private ItemStack mapItem;

    private final Set<Player> members = new HashSet<>();

    public void onGameStarted(ItemDictionary chosenItems) {
        mapImage = new MapImage(chosenItems, this);
        bingoRenderer = new BingoRenderer(mapImage);
        setupMap();

        members.forEach(m -> m.getInventory().setItemInOffHand(mapItem.clone()));
        bingoRenderer.update();
    }

    public boolean isFull() {
        return members.size() == playersPerTeam;
    }

    private void setupMap() {
        mapItem = new ItemStack(Material.FILLED_MAP);

        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        MapView mapView = Bukkit.createMap(game.getWorld());

        mapView.getRenderers().clear();
        mapView.addRenderer(bingoRenderer);

        Objects.requireNonNull(mapMeta).setMapView(mapView);
        mapItem.setItemMeta(mapMeta);
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
     * Broadcasts a message for this team.
     *
     * @param message the message from {@link AsyncPlayerChatEvent#getMessage()}
     */
    public void broadcast(String message) {
        members.forEach(m -> m.sendMessage(Main.PREFIX + message));
    }

    /**
     * Joins the player to this team.
     * This will send out broadcasts and add the player to the {@link Game#getPlayers()} map as well.
     *
     * @param player the {@link Player}
     */
    public void joinPlayer(Player player) {
        members.add(player);

        player.setBedSpawnLocation(spawnPoint, true);
        player.teleport(spawnPoint);

        game.broadcast(String.format("&8[&a+&8] &e%s &9has joined the game.", player.getName()));
        game.addPlayer(player, this);
    }

    /**
     * Removes the player to this team.
     * This will send out broadcasts and add the player to the {@link Game#getPlayers()} map as well.
     *
     * @param player the {@link Player}
     */
    public void removePlayer(Player player) {
        members.remove(player);

        game.broadcast(String.format("&8[&c-&8] &e%s &9has left the game.", player.getName()));
        game.removePlayer(player);
    }
}
