package net.iceyleagons.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.enums.BoardMode;
import net.iceyleagons.bingo.map.MapImage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * @author TOTHTOMI
 */
public class GameUtils {

    public static CompletableFuture<Boolean> pregenerate(World world, int minChunkX, int minChunkY, int maxChunkX, int maxChunkY) {
        return CompletableFuture.supplyAsync(() -> {
            for (int i = minChunkX; i < maxChunkX; i++) {
                for (int j = minChunkY; j < maxChunkY; j++) {
                    int finalI = i;
                    int finalJ = j;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            world.loadChunk(finalI, finalJ, true);
                            world.unloadChunk(finalI, finalJ);
                        }
                    }.runTaskLater(Main.main, i + j);
                }
            }
            return true;
        });
    }

    public static boolean checkForWin(boolean[][] checkMatrix, BoardMode boardMode) {
        try {
            int count = 0;
            int size = MapImage.GRID_SIZE;

            if (boardMode == BoardMode.FULL_HOUSE) {
                /*===Full House===*/
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (!checkMatrix[i][j]) return false; //At least one cell is not checked.
                    }
                }
                return true;

            } else if (boardMode == BoardMode.LINE) {


                for (int i = 0; i < size; i++) {
                    count = 0;
                    for (int j = 0; j < size; j++) {
                        if (checkMatrix[i][j]) {
                            count++;
                            if (count == size)
                                return true; //There are size amount of connected checked cells -> size in a line
                        } else count = 0;
                    }
                }

                for (int i = 0; i < size; i++) {
                    count = 0;
                    for (int j = 0; j < size; j++) {
                        if (checkMatrix[j][i]) {
                            count++;
                            if (count == size)
                                return true; //There are size amount of connected checked cells -> size in a line
                        } else count = 0;
                    }
                }

            } else if (boardMode == BoardMode.SPREAD) {
                // TODO
            } else if (boardMode == BoardMode.DIAGONAL) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; i < size; i++) {
                        for (int m = 0; m < size; m++) {
                            if (checkMatrix[i + m][j + m]) {
                                count++;
                                if (count == size) return true;
                            } else count = 0;
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
            //Ignoring
        }
        return false;
    }

    public static boolean isNumber(String suspectedInteger) {
        try {
            Integer.parseInt(suspectedInteger);
        } catch (NumberFormatException ignored) {
            return false;
        }
        return true;
    }

    public static void allocateTeamLocations(Game game, int range) {
        game.getTeams().values().forEach(team -> {
            Vector2 v2 = Vector2.random(range, range);
            Block block = game.getWorld().getHighestBlockAt(v2.x, v2.z);
            while (block.isLiquid()) {
                v2 = Vector2.random(range, range);
                block = game.getWorld().getHighestBlockAt(v2.x, v2.z);
            }
            Location highest = block.getLocation();

            highest.getChunk().load();

            team.setSpawnLocation(new Location(highest.getWorld(), highest.getBlockX(), highest.getBlockY() + 1, highest.getBlockZ()));
        });
    }

    @Data
    @AllArgsConstructor
    private static class Vector2 {
        int x, z;

        static Random random = new Random();

        public static Vector2 random(int x, int z) {
            int rX = random.nextInt(x);
            int rZ = random.nextInt(z);
            if (random.nextBoolean())
                rX = -rX;
            if (random.nextBoolean())
                rZ = -rZ;
            return new Vector2(rX, rZ);
        }
    }

    public static void spawnFireworks(Location location) {
        Firework fw = (Firework) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.FIREWORK);
        fw.setMetadata("nodamage", new FixedMetadataValue(Main.main, true));
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME, Color.RED, Color.WHITE).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

}
