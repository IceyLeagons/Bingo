package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public class Join {

    @CommandManager.Cmd(cmd = "join")
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        GameManager.joinGame((Player)sender, Main.testGame);
        return CommandManager.CommandFinished.DONE;
    }

}
