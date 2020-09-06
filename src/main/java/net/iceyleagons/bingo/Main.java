package net.iceyleagons.bingo;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import net.iceyleagons.bingo.apis.GlowApiProvider;
import net.iceyleagons.bingo.apis.PartyProvider;
import net.iceyleagons.bingo.apis.PlaceholderProvider;
import net.iceyleagons.bingo.bungee.BungeeMessenger;
import net.iceyleagons.bingo.commands.CommandManager;
import net.iceyleagons.bingo.commands.cmds.Join;
import net.iceyleagons.bingo.commands.cmds.Vote;
import net.iceyleagons.bingo.game.*;
import net.iceyleagons.bingo.listeners.BukkitListeners;
import net.iceyleagons.bingo.listeners.PacketListeners;
import net.iceyleagons.bingo.packet.NettyHandler;
import net.iceyleagons.bingo.packet.PacketEventsHandler;
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
    public static PacketEventsHandler eventsHandler;
    public static BungeeMessenger bungeeMessenger;


    @Override
    public void onEnable() {
        main = this;
        eventsHandler = new PacketEventsHandler();
        eventsHandler.registerListener(new PacketListeners());
        CommandManager commandManager = new CommandManager(this, "Bingo", "bingo.node", "bingo", "b");
        BukkitListeners.register(this);
        ScoreboardLib.setPluginInstance(this);
        commandManager.loadCommandClass(Join.class);
        commandManager.loadCommandClass(Vote.class);
        bungeeMessenger = new BungeeMessenger(this);
       // setupCommands(commandManager);
        /*tinyProtocol = new TinyProtocol(this) {
            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                return super.onPacketInAsync(sender, channel, packet);
            }

            @Override
            public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
                return PacketListeners.handleOut(receiver, channel, packet);
            }
        };*/
        testGame = GameManager.createGame();

        PartyProvider.initIfPresent();
        PlaceholderProvider.registerPlaceholdersIfPluginPresent();
        GlowApiProvider.initIfProtocolPresent();
    }

    private void setupCommands(CommandManager commandManager) {
    }

    @Override
    public void onDisable() {
        NettyHandler.ids.keySet().forEach(PacketUtils::uninjectPlayer);
        BingoPlayer.players.forEach(BingoPlayer::loadPlayerStats);
        BingoPlayer.players.clear();
        WorldManager.cleanup();
    }

    public File getResourceFile(String name) {
        return new File(Main.class.getResource("/"+name).getFile());
    }
}
