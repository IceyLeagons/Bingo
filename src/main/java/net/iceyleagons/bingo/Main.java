package net.iceyleagons.bingo;

import net.iceyleagons.icicle.IcicleBootstrapper;
import nl.rutgerkok.worldgeneratorapi.BiomeGenerator;
import nl.rutgerkok.worldgeneratorapi.event.WorldGeneratorInitEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8[&cBingo&6!&8] &9");
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(this, this);
        IcicleBootstrapper.bootstrap(this, "net.iceyleagons.bingo");
    }

    @EventHandler
    public void onWorldInit(WorldGeneratorInitEvent initEvent) {
        if (initEvent.getWorld().getName().toLowerCase().contains("game")) {
            BiomeGenerator normalGenerator = initEvent.getWorldGenerator().getBiomeGenerator();
            initEvent.getWorldGenerator().setBiomeGenerator((x, y, z) -> normalGenerator.getZoomedOutBiome(x * 10, y, z * 10));
        }
    }
}
