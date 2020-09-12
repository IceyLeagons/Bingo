package net.iceyleagons.bingo.game;

import lombok.Getter;
import lombok.Setter;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import net.iceyleagons.bingo.game.teams.Team;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author TOTHTOMI
 * <p>
 * (because the server sucks we cannot use @Data)
 */

public class BingoPlayer {

    @Setter
    @Getter
    public static List<BingoPlayer> players = new ArrayList<>();
    @Setter
    @Getter
    private Game game;
    @Setter
    @Getter
    private Team team;
    @Setter
    @Getter
    private Location location;
    @Setter
    @Getter
    private ItemStack[] armorContents = new ItemStack[0];
    @Setter
    @Getter
    private ItemStack[] inventoryContents = new ItemStack[0];
    @Setter
    @Getter
    private ItemStack[] extraContents = new ItemStack[0];
    @Setter
    @Getter
    private ItemStack[] storageContents = new ItemStack[0];
    @Setter
    @Getter
    private Location spawnpoint;
    @Setter
    @Getter
    private Scoreboard scoreboard;
    @Setter
    @Getter
    private List<BingoPlayer> stacked = new ArrayList<>();
    @Setter
    @Getter
    private BingoPlayer topMounted = this;
    @Setter
    @Getter
    private BingoPlayer mountedOn = null;
    @Setter
    @Getter
    private long debounceTill;
    @Setter
    @Getter
    private Player player;
    @Setter
    @Getter
    private String playerListName;
    @Setter
    @Getter
    private float exp = 0;
    @Setter
    @Getter
    private float exhaustion = 0;
    @Setter
    @Getter
    private int foodLevel = 20;
    @Setter
    @Getter
    private double health = 20.0;
    @Setter
    @Getter
    private float saturation = 20;
    @Getter
    @Setter
    private Integer freezedPlayerId;
    @Getter
    @Setter
    private double maxHealth = 20.0;

    public BingoPlayer(Player player) {
        this.player = player;
        players.add(this);
    }

    /**
     * Stacks wantsToStack onto this bingo player. Easy, right?
     *
     * @param wantsToStack the one - who as the name implies - wants to stack.
     */
    public void stackPlayer(BingoPlayer wantsToStack) {
        if (wantsToStack.getMountedOn() == null) {
            wantsToStack.setMountedOn(this);
            if (mountedOn == null) {
                stacked.add(wantsToStack);

                resolveStacks();
            } else
                mountedOn.stackPlayer(wantsToStack);
        }
    }

    public void removeStacked(BingoPlayer leave) {
        if (leave.getMountedOn() == this) {
            for (Entity entity : player.getPassengers())
                if (entity instanceof ArmorStand) {
                    player.removePassenger(entity);
                    entity.remove();
                }

            leave.setMountedOn(null);
            stacked.remove(leave);

            resolveStacks();
        }
    }

    private void resolveStacks() {
        debounceTill = System.currentTimeMillis() + 30L;
        for (Entity entity : player.getPassengers()) {
            player.removePassenger(entity);
            if (entity instanceof ArmorStand)
                entity.remove();
        }

        for (BingoPlayer stack : stacked) {
            ArmorStand armorStand = (ArmorStand) stack.getPlayer().getWorld().spawnEntity(stack.getPlayer().getLocation(), EntityType.ARMOR_STAND);
            armorStand.setSmall(true);
            armorStand.setVisible(false);
            armorStand.setBasePlate(false);
            armorStand.setCollidable(false);
            armorStand.setInvulnerable(true);

            for (EquipmentSlot value : EquipmentSlot.values())
                armorStand.addEquipmentLock(value, ArmorStand.LockType.ADDING_OR_CHANGING);

            topMounted.getPlayer().addPassenger(armorStand);
            armorStand.addPassenger(stack.getPlayer());
            topMounted = stack;
        }

        topMounted = this;
    }

    /**
     * Used for saving the player's stats before resetting them so they can play.
     */
    public void savePlayerStats() {
        armorContents = player.getInventory().getArmorContents();
        inventoryContents = player.getInventory().getContents();
        extraContents = player.getInventory().getExtraContents();
        storageContents = player.getInventory().getStorageContents();
        exp = player.getExp();
        playerListName = player.getPlayerListName();
        location = player.getLocation();
        exhaustion = player.getExhaustion();
        foodLevel = player.getFoodLevel();
        health = player.getHealth();
        maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        if (player.getBedSpawnLocation() != null)
            spawnpoint = player.getBedSpawnLocation();
        saturation = player.getSaturation();
    }

    public void resetPlayerStats() {
        player.getInventory().clear();
        player.setExhaustion(0);
        player.setExp(0);
        player.setFoodLevel(20);
        player.setHealth(1);
        player.setSaturation(20);
    }

    public void loadPlayerStats() {
        Objects.requireNonNull(Objects.requireNonNull(player.getPlayer()).getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
        player.getInventory().setArmorContents(armorContents);
        player.getInventory().setContents(inventoryContents);
        player.getInventory().setExtraContents(extraContents);
        player.getInventory().setStorageContents(storageContents);
        if (spawnpoint != null)
            player.setBedSpawnLocation(spawnpoint, false);
        player.setPlayerListName(playerListName);
        player.setExp(exp);
        player.setExhaustion(exhaustion);
        player.setFoodLevel(foodLevel);
        player.setHealth(health);
        player.setSaturation(saturation);
        player.teleport(location);
    }

}
