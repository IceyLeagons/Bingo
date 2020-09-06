package net.iceyleagons.bingo.apis;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * @author TOTHTOMI
 */
public class PartyProvider {

    @Getter
    private static PartiesAPI api;
    @Getter
    private static boolean enabled = false;

    public static void init() {
        if (!enabled) {
            api = Parties.getApi();
            enabled = true;
        }
    }

    public static void initIfPresent() {
        if (isPresent()) init();
    }

    public static boolean isPresent() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Parties") != null) {
            return Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Parties")).isEnabled();
        }
        return false;
    }

    public static PartyPlayer getPartyPlayer(Player player) {
        return api.getPartyPlayer(player.getUniqueId());
    }

    public static boolean isPartOfParty(Player player) {
        PartyPlayer partyPlayer = getPartyPlayer(player);
        return api.getParty(partyPlayer.getPartyName()) != null;
    }

    public static Party getParty(Player player) {
        return getParty(getPartyPlayer(player));
    }

    public static Party getParty(PartyPlayer partyPlayer) {
        return api.getParty(partyPlayer.getPartyName());
    }


}
