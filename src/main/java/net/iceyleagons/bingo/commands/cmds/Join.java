package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.game.GameManager;
import net.iceyleagons.bingo.utils.InventoryFactory;
import net.iceyleagons.bingo.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 */
public class Join {

    @CommandManager.Cmd(cmd = "join", enablePermCheck = false)
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        if (sender instanceof Player) {
            Collection<Game> games = Game.games.values().stream().parallel().sorted(Comparator.comparingInt(Game::getPlayerNumber)).collect(Collectors.toList());
            int gamesSize = Game.games.size();
            InventoryFactory factory = new InventoryFactory("Games", gamesSize <= 5 ? 5 : gamesSize <= 9 ? 9 : gamesSize <= 27 ? 27 : 54, true);
            int i = 0;
            for (Game game : games)
                if (game.getPlayerNumber() != game.getMaxPlayers()) {
                    ItemStack itemStack = ItemFactory.newFactory(Material.DIAMOND_AXE).setAmount(1).setDisplayName("&6Game #" + game.getId())
                            .setLore(Arrays.asList("&7Players: " + game.getPlayerNumber() + " / " + game.getMaxPlayers(),
                                    "&7Number of teams: " + game.getAmountOfTeams())).build();
                    factory.setItem(itemStack,
                            i, new InventoryFactory.ClickRunnable() {
                                @Override
                                public void run(InventoryClickEvent e) {
                                    GameManager.joinGame((Player) e.getWhoClicked(), game, true, null);
                                }
                            });
                    i++;
                }
            factory.openInventory((Player) sender);

            return CommandManager.CommandFinished.DONE;
        } else
            return CommandManager.CommandFinished.NOCONSOLE;
    }

}
