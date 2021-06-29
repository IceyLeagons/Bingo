package net.iceyleagons.bingo;

import net.iceyleagons.icicle.IcicleBootstrapper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8[&cBingo&6!&8] &9");
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        IcicleBootstrapper.bootstrap(this, "net.iceyleagons.bingo");
    }
}
