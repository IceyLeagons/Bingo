package net.iceyleagons.bingo.apis;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    /**
     * @param receiver
     * @param glow
     * @param who
     * @deprecated don't use, unless you are stupid.
     */
    @Deprecated
    public static void forceGlow(Player receiver, boolean glow, Player... who) {
        for (Player player : who)
            external.protocol_glowapi.GlowAPI.setGlowingAsync(player, glow, receiver);
    }

    public static void setGlowIfPresent(Player receiver, boolean glow, Player... who) {
        if (isProtocolPresent())
            forceGlow(receiver, glow, who);
    }

}
