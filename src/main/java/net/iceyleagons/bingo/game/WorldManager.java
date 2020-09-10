package net.iceyleagons.bingo.game;

import net.iceyleagons.bingo.apis.WorldGeneratorProvider;
import org.bukkit.*;

import java.util.HashMap;
import java.util.Map;
import java.io.File;

/**
 * @author TOTHTOMI
 */
public class WorldManager {

    private static Map<Game, World> worlds = new HashMap<>();

    public static void deleteGameWorld(Game game) {
        World target = worlds.get(game);
        Chunk[] chunks = target.getLoadedChunks();
        for (Chunk chunk : chunks)
            chunk.unload(false);

        File toDelete = target.getWorldFolder();
        if (Bukkit.getServer().unloadWorld(target, false))
            if (!toDelete.delete())
                throw new RuntimeException("Failed to delete world for game " + game.getId());

        worlds.remove(game);
    }

    public static void cleanup() {
        worlds.keySet().forEach(WorldManager::deleteGameWorld);
    }

    public static World[] allocateWorld(Game game) {
        String worldName = "Game-" + game.getId();
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generateStructures(true);
        worldCreator.type(WorldType.NORMAL);
        if (WorldGeneratorProvider.isPresent())
            worldCreator.generator(WorldGeneratorProvider.getGenerator(worldName));
        World world = worldCreator.createWorld();
        assert world != null;
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(1024D);
        worlds.put(game, world);

        /*
        WorldCreator netherCreator = new WorldCreator("GameN-" + game.getId());

        netherCreator.environment(World.Environment.NETHER);
        netherCreator.generateStructures(true);
        netherCreator.type(WorldType.NORMAL);
        World nether = netherCreator.createWorld();
        assert nether != null;
        nether.getWorldBorder().setCenter(0,0);
        nether.getWorldBorder().setSize(128D);

         */
        // worlds.put(game,nether);


        return new World[]{world};
    }

}
