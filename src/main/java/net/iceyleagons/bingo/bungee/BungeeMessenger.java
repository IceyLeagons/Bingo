package net.iceyleagons.bingo.bungee;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author TOTHTOMI
 */
public class BungeeMessenger implements PluginMessageListener {

    public static final String MAIN_SUBCHANNEL = "ILBingo";


    @Getter
    private JavaPlugin plugin;
    @Getter
    private Map<String, BingoMessageHandler> handlerMap;

    public BungeeMessenger(JavaPlugin javaPlugin) {
        javaPlugin.getServer().getMessenger().registerOutgoingPluginChannel(javaPlugin, "BungeeCord");
        javaPlugin.getServer().getMessenger().registerIncomingPluginChannel(javaPlugin, "BungeeCord", this);
        handlerMap = new HashMap<>();
    }

    public void registerHandler(BingoMessageHandler messageHandler) {
        handlerMap.put(messageHandler.getSubchannel(),messageHandler);
    }

    public void handleMessage(String subchannel, Player player, String msg) {
        if (handlerMap.containsKey(subchannel)) {
            handlerMap.get(subchannel).handle(msg,player);
        }
    }

    public void sendMessage(String subchannel, String msg) {
        sendMessage(subchannel, msg, Objects.requireNonNull(Iterables.getFirst(Bukkit.getOnlinePlayers(), null)));
    }

    public void sendMessage(String subchannel, String msg, Player sender) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        out.writeUTF(msg);

        sender.sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        String commandMessage = in.readUTF();
        handleMessage(subchannel, player, commandMessage);
    }
}
