package net.iceyleagons.bingo.apis;

import org.bukkit.Bukkit;

public class WorldGeneratorProvider {

    public static boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("WorldGeneratorApi") != null;
    }

}
