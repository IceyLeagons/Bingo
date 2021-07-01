package net.iceyleagons.bingo.utils;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {

    private static final String[] NUMBERS = "➊ ➋ ➌ ➍ ➎ ➏ ➐ ➑ ➒ ➓".split(" ");

    public static List<Block> nonBreakables = new ArrayList<>();

    public static String getNumberIcon(int number) {
        int n = Math.max(number - 1, 0);
        if (NUMBERS.length <= n) return "";

        return NUMBERS[n];
    }

    private static Block getBlock(Location location) {
        nonBreakables.add(location.getBlock());
        return location.getBlock();
    }

    private static Material getWoolForDye(DyeColor color) {
        try {
            return Material.valueOf(color.name() + "_WOOL");
        } catch (IllegalArgumentException e) {
            return Material.QUARTZ_BLOCK;
        }
    }

    public static void setBorderForPlayer(Player player, int size, Location center) {

    }

    public static void spawnPlatform(DyeColor color, Location spawnLocation) {
        getBlock(spawnLocation.clone().add(0, -1, 0)).setType(getWoolForDye(color));


        for (int x = -2; x < 3; x++)
            for (int z = -2; z < 3; z++)
                if (x == -2 || x == 2 || z == -2 || z == 2)
                    getBlock(spawnLocation.clone().add(x, -1, z)).setType(Material.SMOOTH_QUARTZ);
                else if (x == 0 || z == 0)
                    getBlock(spawnLocation.clone().add(x, -1, z)).setType(getWoolForDye(color));
                else
                    getBlock(spawnLocation.clone().add(x, -1, z)).setType(Material.QUARTZ_BLOCK);

        for (int y = 0; y < 6; y++)
            for (int x = -2; x < 3; x++)
                for (int z = -2; z < 3; z++)
                    spawnLocation.clone().add(x, y, z).getBlock().setType(Material.AIR);
    }
}
