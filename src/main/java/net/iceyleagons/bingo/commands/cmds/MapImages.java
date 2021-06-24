package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TOTHTOMI
 */
public class MapImages {

    @CommandManager.Cmd(cmd = "maps", enablePermCheck = true )
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(player,54,"Maps");
        if (GameManager.isInGame(player)) {
            AtomicInteger count = new AtomicInteger();
            GameManager.getGame(player).getTeams().values().forEach(team -> {
                ItemStack itemStack = team.getMapItem().clone();
                ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                itemMeta.setDisplayName(team.getTeamName());
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(count.get(),itemStack);
                count.getAndIncrement();
            });
        }
        player.openInventory(inventory);

        return CommandManager.CommandFinished.DONE;
    }

}
