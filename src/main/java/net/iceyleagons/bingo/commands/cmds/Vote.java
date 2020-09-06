package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.BingoPlayer;
import net.iceyleagons.bingo.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public class Vote {

    @CommandManager.Cmd(cmd = "vote")
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        Player player = (Player) sender;
        if (GameManager.isInGame(player)) {
            GameManager.getGame(player).getVote().open(player);
        }
        return CommandManager.CommandFinished.DONE;
    }

}
