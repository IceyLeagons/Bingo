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

    private static int chosenMethod = 0;

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

    private static int chooseMethod() {
        if (!enabled && isProtocolPresent())
            return chosenMethod = 2;
        else if (isPresent())
            return chosenMethod = 1;

        return chosenMethod = 0;
    }

    /**
     * @param receiver
     * @param glow
     * @param who
     * @deprecated don't use, unless you are stupid.
     */
    @Deprecated
    public static void forceGlow(Player receiver, boolean glow, Player... who) {
        switch (chosenMethod) {
            case 1:
                for (Player player : who)
                    GlowAPI.setGlowing(player, glow, receiver);

                enabled = true;
                break;
            case 2:
                for (Player player : who)
                    external.protocol_glowapi.GlowAPI.setGlowingAsync(player, glow, receiver);
                break;
            case 0:
            default:
                break;
        }
    }

    public static void setGlowIfPresent(Player receiver, boolean glow, Player... who) {
        if (chosenMethod == 0)
            chooseMethod();

        forceGlow(receiver, glow, who);
    }

}
