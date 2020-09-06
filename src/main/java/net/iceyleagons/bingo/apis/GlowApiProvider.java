package net.iceyleagons.bingo.apis;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

public class GlowApiProvider {

    @Getter
    private static boolean enabled = false;

    @Getter
    private static external.protocol_glowapi.GlowAPI protocolLibApi;

    private static void init() {
        if (!enabled) {
            protocolLibApi = new external.protocol_glowapi.GlowAPI();
            protocolLibApi.onEnable();

            enabled = true;
        }
    }

    public static void initIfProtocolPresent() {
        if (isProtocolPresent())
            init();
    }

    public static boolean isProtocolPresent() {
        return Bukkit.getPluginManager().getPlugin("ProtocolLib") != null;
    }

    public static boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("GlowAPI") != null;
    }

    public static void setGlowIfPresent(Player receiver, boolean glow, Player... who) {
        if (enabled && isProtocolPresent())
            for (Player player : who)
                external.protocol_glowapi.GlowAPI.setGlowingAsync(player, glow, receiver);
        else if (isPresent()) {
            for (Player player : who)
                GlowAPI.setGlowing(player, glow, receiver);

            enabled = true;
        }
    }

}
