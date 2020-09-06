package net.iceyleagons.bingo.game.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public enum GameMode {

    AMATEUR(false, true, false, true, Integer.MAX_VALUE, 300, 300, Integer.MAX_VALUE, 0, 13, 8, 4, 0, 0, "Recommended for an easy game of bingo. No hard feelings at all.", false, 0),
    NORMAL(true, true, false, true, 10, 0, 180, 600, 5, 6, 9, 6, 3, 1, "For people who want to have a bit more challenge but are up to going against others.", false, 0),
    INSANITY(true, false, true, false, 0, 0, 0, 0, 5, 0, 2, 8, 10, 5, "If you want to experience hell, I'd recommend pairing this with full house!", true, 4);

    @NonNull
    @Getter
    private boolean pvp, keepInventory, smallerMap, respawnOnLeader;

    @NonNull
    @Getter
    private int respawns, resistanceTime, absorptionTime, gracePeriod, shrinkSpeed, free, easy, medium, hard, expert;

    @NonNull
    private String description;

    @NonNull
    @Getter
    private boolean changeHearts;

    @NonNull
    @Getter
    private int totalNumOfHearts;

    private String cachedDesc = null;

    public String getDescription() {
        if (cachedDesc != null)
            return cachedDesc;

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY + description).append("\n\n" + ChatColor.RED + "This mode has: ").append("\n");
        builder.append(ChatColor.RED + " - PvP " + (pvp ? "enabled!" : "disabled!")).append("\n");
        builder.append(ChatColor.RED + " - Keep Inventory " + (keepInventory ? "enabled!" : "disabled!")).append("\n");
        builder.append(ChatColor.RED + (respawns > 100 ? " - Unlimited respawns!" :
                (respawns == 0 ? " - No respawns at all! Hardcore!"
                        : " - " + respawns + " respawn" + (respawns != 1 ? "s" : "") + " in total!"))).append("\n");
        builder.append(ChatColor.RED + (resistanceTime > 0 ? " - " + (resistanceTime / 60) + "m of absorption" + (absorptionTime > 0 ? (absorptionTime / 60) + "m of fire resistance!" : "!")
                : (absorptionTime > 0 ? " - " + (absorptionTime / 60) + "m of absorption" : ""))).append("\n");
        if (!(gracePeriod > 10000))
            builder.append(ChatColor.RED + (gracePeriod > 0 ? " - " + (gracePeriod / 60) + "m of grace period!" : " - No grace period!")).append("\n");
        builder.append(ChatColor.RED + (shrinkSpeed == 0 ? " - No world border shrinking!" : " - A world border shrink speed of " + shrinkSpeed + " blocks per minute!")).append("\n");
        builder.append(ChatColor.RED + (smallerMap ? " - A smaller map! (256x256)" : " - A normal sized map! (512x512)"));

        return cachedDesc = builder.toString();
    }

}
