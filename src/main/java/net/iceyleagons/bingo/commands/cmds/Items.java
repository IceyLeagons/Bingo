package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.GameManager;
import net.iceyleagons.bingo.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TOTHTOMI
 */
public class Items {

    @CommandManager.Cmd(cmd = "items", enablePermCheck = true )
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(player,54,"Items");
        if (GameManager.isInGame(player)) {
            int i = 0;
            for (Material value : GameManager.getGame(player).getItems().values()) {
                inventory.setItem(i,ItemFactory.newFactory(value).build());
                i++;
            }
        }
        player.openInventory(inventory);

        return CommandManager.CommandFinished.DONE;
    }

}
