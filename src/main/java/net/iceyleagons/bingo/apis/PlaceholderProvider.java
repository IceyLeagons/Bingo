package net.iceyleagons.bingo.apis;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import lombok.Getter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author TOTHTOMI
 */
public class PlaceholderProvider {

    @Getter
    private static boolean enabled = false;

    public static boolean isPresent() {
        return Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    private static List<PlaceholderExpansion> expansions = new ArrayList<>();

    public static void addExpansion(PlaceholderExpansion expansion) {
        expansions.add(expansion);
    }

    public static void registerPlaceholdersIfPluginPresent() {
        if (isPresent()) {
            expansions.forEach(e -> {
                if (e.canRegister()) {
                    e.register();
                }
            });
        }
    }

}
