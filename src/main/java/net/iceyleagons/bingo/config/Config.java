package net.iceyleagons.bingo.config;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.text.html.Option;
import java.io.File;
import java.util.Optional;

/**
 * @author TOTHTOMI
 */
public abstract class Config {

    @Getter
    private final File file;
    @Getter
    private YamlConfiguration configuration;

    public Config(Optional<File> subFolder, String name, JavaPlugin javaPlugin) {
        file = subFolder.map(value ->
                new File(javaPlugin.getDataFolder() + File.separator + value, name + ".yml")).orElseGet(() ->
                new File(javaPlugin.getDataFolder(), name + ".yml"));
    }

    public abstract void loadDefaults();

    private void loadConfig() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    @SneakyThrows
    public void saveConfig() {
        configuration.save(file);
    }

    public void reloadConfig() {
        loadConfig();
        saveConfig();
    }

}
