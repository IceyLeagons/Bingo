package net.iceyleagons.bingo.apis;

import net.iceyleagons.bingo.Main;
import nl.rutgerkok.worldgeneratorapi.BiomeGenerator;
import nl.rutgerkok.worldgeneratorapi.WorldGeneratorApi;
import nl.rutgerkok.worldgeneratorapi.WorldRef;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class WorldGenerator {

    public static ChunkGenerator createChunkGen(String world) {
        return WorldGeneratorApi.getInstance(Main.main, 0, 5).createCustomGenerator(WorldRef.ofName(world), generator -> {
            BiomeGenerator vanillaBiomeGenerator = generator.getBiomeGenerator();
            generator.setBiomeGenerator((x, y, z) -> vanillaBiomeGenerator.getZoomedOutBiome(x * 16, y, z * 16));
        });
    }

}
