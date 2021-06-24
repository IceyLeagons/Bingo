package net.iceyleagons.bingo;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import net.iceyleagons.bingo.apis.GlowApiProvider;
import net.iceyleagons.bingo.apis.PartyProvider;
import net.iceyleagons.bingo.apis.PlaceholderProvider;
import net.iceyleagons.bingo.apis.WorldGenerator;
import net.iceyleagons.bingo.bungee.BungeeMessenger;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.commands.cmds.*;
import net.iceyleagons.bingo.config.MainConfig;
import net.iceyleagons.bingo.game.*;
import net.iceyleagons.bingo.listeners.BukkitListeners;
import net.iceyleagons.bingo.texture.MaterialTexture;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author TOTHTOMI
 */
public class Main extends JavaPlugin implements CommandExecutor {

    public static final String prefix = "&8[&cBingo&e!&8]&r ";
    public static Main main;
    public static Game testGame;
    public static BungeeMessenger bungeeMessenger;
    public static MainConfig mainConfig;

    @Override
    public void onEnable() {
        main = this;
        new MaterialTexture();
        Bukkit.getPluginManager().registerEvents(new WorldGenerator(), this);

        CommandManager commandManager = new CommandManager(this, "Bingo", "bingo.node", "bingo", "b");
        setupCommands(commandManager);

        BukkitListeners.register(this);
        ScoreboardLib.setPluginInstance(this);
        bungeeMessenger = new BungeeMessenger(this);
        //mainConfig = new MainConfig(this);

        GameManager.loadPlayersFromDatabase();

        testGame = GameManager.createGame(6,3,6,true); // 4 plr/team

        PartyProvider.initIfPresent();
        PlaceholderProvider.registerPlaceholdersIfPluginPresent();
        GlowApiProvider.initIfProtocolPresent();
    }

    private void setupCommands(CommandManager commandManager) {
        commandManager.loadCommandClass(CreateGame.class);
        commandManager.loadCommandClass(Join.class);
        commandManager.loadCommandClass(Vote.class);
        commandManager.loadCommandClass(MapImages.class);
        commandManager.loadCommandClass(Start.class);
        commandManager.loadCommandClass(SaveStructure.class);
        commandManager.loadCommandClass(Items.class);
    }

    @Override
    public void onDisable() {
        BingoPlayer.players.forEach(BingoPlayer::loadPlayerStats);
        GameManager.savePlayersToDatabase();
        BingoPlayer.players.clear();
        WorldManager.cleanup();
    }

    public File getResourceFile(String name) {
        return new File(Main.class.getResource("/" + name).getFile());
    }
}
