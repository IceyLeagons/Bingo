package net.iceyleagons.bingo;

import net.iceyleagons.icicle.IcicleBootstrapper;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        IcicleBootstrapper.bootstrap(this, "net.iceyleagons.bingo");
    }
}
