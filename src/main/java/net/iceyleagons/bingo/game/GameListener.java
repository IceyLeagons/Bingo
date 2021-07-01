package net.iceyleagons.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.bingo.game.team.Team;
import net.iceyleagons.bingo.game.team.TeamProgressHandler;
import net.iceyleagons.bingo.items.MapImage;
import net.iceyleagons.bingo.utils.GameUtils;
import net.iceyleagons.bingo.utils.XYCoordinate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class GameListener implements Listener {

    private final Game game;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) return;

        if (event.getTo() == null) return;
        if (game.getGameState() != GameState.IN_GAME)
            if (game.getPlayers().get(event.getPlayer()).getSpawnPoint().toVector().setY(0).distance(event.getTo().toVector().setX(0)) > 3)
                event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameUtils.nonBreakables.contains(event.getBlock()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (!game.getPlayers().containsKey(event.getPlayer())) return;

        game.getGameManager().leave(event.getPlayer());
    }

    @EventHandler
    public void onBingoItemEvent(BingoItemEvent event) {
        MapImage mapImage = event.getTeam().getMapImage();
        TeamProgressHandler teamProgressHandler = event.getTeam().getTeamProgressHandler();

        if (mapImage == null || teamProgressHandler == null) {
            System.out.println("MapImage or TeamProgressHandler return null.");
            return; //How TF??
        }

        if (mapImage.getCoordinates().containsKey(event.getMaterial())) {
            event.getTeam().broadcast(ChatColor.translateAlternateColorCodes('&', String.format("&e%s &9has found &e%s", event.getPlayer().getName(), event.getMaterial().getKey().getKey())));

            XYCoordinate xyCoordinate = mapImage.getCoordinates().get(event.getMaterial());
            if (!teamProgressHandler.isChecked(xyCoordinate)) teamProgressHandler.setChecked(xyCoordinate, true);

            event.getTeam().getBingoRenderer().update();
            teamProgressHandler.checkForWin(game.getBoardMode());
        }
    }

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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChat(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ;

        Material material = event.getItem().getItemStack().getType();
        Player player = (Player) event.getEntity();
        Team team = game.getPlayers().get(player);

        Bukkit.getServer().getPluginManager().callEvent(new BingoItemEvent(player, team, material));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChat(CraftItemEvent event) {
        Material material = event.getRecipe().getResult().getType();

        Player player = (Player) event.getWhoClicked();
        Team team = game.getPlayers().get(player);

        Bukkit.getServer().getPluginManager().callEvent(new BingoItemEvent(player, team, material));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChat(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        Material material = event.getCurrentItem().getType();
        Player player = (Player) event.getWhoClicked();
        Team team = game.getPlayers().get(player);

        Bukkit.getServer().getPluginManager().callEvent(new BingoItemEvent(player, team, material));
    }

}
