package net.iceyleagons.bingo.apis;

import nl.rutgerkok.worldgeneratorapi.BiomeGenerator;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationType;
import nl.rutgerkok.worldgeneratorapi.event.WorldGeneratorInitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WorldGenerator implements Listener {

    @EventHandler
    public void onInit(WorldGeneratorInitEvent event) {
        if (event.getWorld().getName().toLowerCase().contains("game")) {
            event.getWorldGenerator().getWorldDecorator().getCustomDecorations(DecorationType.UNDERGROUND_ORES);
            BiomeGenerator normalGenerator = event.getWorldGenerator().getBiomeGenerator();
            event.getWorldGenerator().setBiomeGenerator((x, y, z) -> normalGenerator.getZoomedOutBiome(x * 8, y, z * 8));
        }
    }

}
