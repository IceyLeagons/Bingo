package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public class Start {
    @CommandManager.Cmd(cmd = "start")
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        if (!sender.isOp()) return CommandManager.CommandFinished.PERMISSION;

        Player player = (Player) sender;
        if (GameManager.isInGame(player)) {
            boolean bool = GameManager.getGame(player).forceStartGame();
            if (!bool)
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.prefix)+"§4At least 2 players are required to start a game!");

        }

        return CommandManager.CommandFinished.DONE;
    }

}
