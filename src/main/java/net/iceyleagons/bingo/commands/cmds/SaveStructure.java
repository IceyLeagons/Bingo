package net.iceyleagons.bingo.commands.cmds;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.game.GameManager;
import net.iceyleagons.bingo.utils.festruct.Cuboid;
import net.iceyleagons.bingo.utils.festruct.FEStructSaver;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collections;

public class SaveStructure {

    @CommandManager.Cmd(cmd = "save", args = "<lowerX> <lowerY> <lowerZ> <higherX> <higherY> <higherZ> <name>",
            argTypes = {CommandManager.Arg.ArgInteger.class, // x Corner
                    CommandManager.Arg.ArgInteger.class, // y Corner
                    CommandManager.Arg.ArgInteger.class, // z Corner
                    CommandManager.Arg.ArgInteger.class, // 2nd x Corner
                    CommandManager.Arg.ArgInteger.class, // 2nd y Corner
                    CommandManager.Arg.ArgInteger.class, // 2nd z corner
                    CommandManager.Arg.ArgString.class, // name
            }, only = CommandManager.CommandOnly.PLAYER)
    public static CommandManager.CommandFinished onCommand(CommandSender sender, Object[] args) {
        Vector lowerCorner = new Vector((int) args[0], (int) args[1], (int) args[2]);
        Vector higherCorner = new Vector((int) args[3], (int) args[4], (int) args[5]);
        String name = (String) args[6];
        Player player = (Player) sender;

        FEStructSaver saver = new FEStructSaver(new Cuboid(new Location(player.getWorld(), lowerCorner.getX(), lowerCorner.getY(), lowerCorner.getZ()),
                new Location(player.getWorld(), higherCorner.getX(), higherCorner.getY(), higherCorner.getZ())),
                name, Collections.emptyList(), 0);

        saver.save(player);
        return CommandManager.CommandFinished.DONE;
    }

}
