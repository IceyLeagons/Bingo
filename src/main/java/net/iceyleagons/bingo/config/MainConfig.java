package net.iceyleagons.bingo.config;

import net.iceyleagons.bingo.Main;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Optional;

/**
 * @author TOTHTOMI
 */
public class MainConfig extends Config{

    public MainConfig(JavaPlugin plugin) {
        super(Optional.empty(), "config", plugin);
        loadDefaults();
    }

    @Override
    public void loadDefaults() {
        YamlConfiguration configuration = super.getConfiguration();


        configuration.addDefault("updater.check-updates",true);
        configuration.addDefault("updater.auto-update",false);

        configuration.addDefault("language.default-lang","EN");
        configuration.addDefault("language.allow-per-player-lang",false);

        configuration.addDefault("bungee-mode",false);

        super.saveConfig();
    }
}
