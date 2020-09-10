package net.iceyleagons.bingo.apis;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class WorldGeneratorProvider {

    public static boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("WorldGeneratorApi") != null;
    }

    public static ChunkGenerator getGenerator(String world) {
        if (!isPresent())
            return null;
        else
            return WorldGenerator.createChunkGen(world);
    }

}
