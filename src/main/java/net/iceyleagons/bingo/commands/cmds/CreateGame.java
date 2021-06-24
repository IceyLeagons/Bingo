package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.game.GameManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 */
public class CreateGame {

    @CommandManager.Cmd(cmd = "create", args = "<max-players> <amount-of-teams> <starting-player-count>", enablePermCheck = true ,
            argTypes = {CommandManager.Arg.ArgInteger.class, CommandManager.Arg.ArgInteger.class, CommandManager.Arg.ArgInteger.class})
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        Integer maxPlayers = (Integer) args[0];
        Integer amountOfPlayers = (Integer) args[1];
        Integer starting = (Integer) args[2];

        Game game = GameManager.createGame(maxPlayers, amountOfPlayers, starting, true);
        sender.sendMessage("§6Id of the created game is: " + game.getId());
        return CommandManager.CommandFinished.DONE;
    }

}
