package net.iceyleagons.bingo.listeners;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.utils.PacketUtils;
import net.iceyleagons.bingo.apis.GlowApiProvider;
import net.iceyleagons.bingo.game.BingoPlayer;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.game.GameManager;
import net.iceyleagons.bingo.game.GameUtils;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.bingo.game.teams.Team;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;
import java.util.Objects;

/**
 * @author TOTHTOMI
 */
public class BukkitListeners implements Listener {

    private static boolean registered = false;

    public static void register(JavaPlugin javaPlugin) {
        if (!registered) {
            Bukkit.getServer().getPluginManager().registerEvents(new BukkitListeners(), javaPlugin);
            registered = true;
        }
    }

    @EventHandler
    public void onBingoEvent(BingoItemEvent event) {
        if (event.getTeam() == null || event.getItemStack() == null || event.getPlayer() == null) return;
        event.getTeam().checkItem(event.getItemStack(), event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PacketUtils.injectPlayer(event.getPlayer());
    }

    //For crafting
    @EventHandler
    public void onPlayerCraftItem(CraftItemEvent itemEvent) {
        if (!(itemEvent.getWhoClicked() instanceof Player)) return;
        Player player = (Player) itemEvent.getWhoClicked();
        callEvent(player, itemEvent.getCurrentItem());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent chatEvent) {
        Player player = chatEvent.getPlayer();
        if (GameManager.isInGame(player)) {
            chatEvent.setCancelled(true);
            BingoPlayer bingoPlayer = GameManager.getBingoPlayer(player);

            if (chatEvent.getMessage().startsWith("!")) {
                bingoPlayer.getGame().globalMessage(bingoPlayer, chatEvent.getMessage().replaceFirst("!", ""));
                return;
            }

            bingoPlayer.getTeam().teamMessage(player, chatEvent.getMessage());
        }
    }

    //For chests/other containers
    @EventHandler
    public void onPlayerClicksInventory(InventoryClickEvent itemEvent) {
        if (!(itemEvent.getWhoClicked() instanceof Player)) return;
        Player player = (Player) itemEvent.getWhoClicked();
        if (itemEvent.getCurrentItem() != null)
            if (GameManager.isInGame(player)) {
            } else {
                Game game = GameManager.getBingoPlayer(player).getGame();
                if (game != null)
                    if (game.getGameState() != null)
                        if (game.getGameState() == GameState.IN_GAME)
                            callEvent(player, itemEvent.getCurrentItem());
            }
    }

    @EventHandler
    public void onPortal(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (GameManager.isInGame(player)) {
            Game game = GameManager.getGame(player);
            if (event.getFrom().equals(game.getWorld())) {
                int highestBlockNotCeiling = 127;
                World nether = game.getNether();
                Block at;
                while ((at = nether.getBlockAt(0, highestBlockNotCeiling, 0)).getType() != Material.OBSIDIAN)
                    highestBlockNotCeiling--;

                event.getPlayer().teleport(at.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            } else if (event.getFrom().equals(game.getNether()))
                event.getPlayer().teleport(new Location(game.getWorld(), 0, game.getWorld().getHighestBlockYAt(0, 0), 0), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @EventHandler
    public void onSignWritten(SignChangeEvent event) {
        if (Objects.equals(event.getLine(0), "[Bingo]")) {
            if (GameUtils.isNumber(event.getLine(1))) {
                int enteredId = Integer.parseInt(Objects.requireNonNull(event.getLine(1)));
                if (GameManager.getGameById(enteredId) == null) {
                    event.setLine(1, "Not Found!");
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Main.prefix + "A game with the id " + enteredId + " not found."));
                }

            }
        }
    }

    @EventHandler
    public void onPlayerTriesToRemoveMap(PlayerSwapHandItemsEvent itemEvent) {
        //if (!GameManager.isInGame(itemEvent.getPlayer())) return;
        //itemEvent.setCancelled(true);
    }

    //For picking up items
    @EventHandler
    public void onPlayerPicksUpItem(EntityPickupItemEvent itemEvent) {
        if (!(itemEvent.getEntity() instanceof Player)) return;
        Player player = (Player) itemEvent.getEntity();
        callEvent(player, itemEvent.getItem().getItemStack());
    }

    @EventHandler
    public void onRightClickTeammate(PlayerInteractEntityEvent event) {
        BingoPlayer player = GameManager.getBingoPlayer(event.getPlayer());
        if (player.getTeam() != null) {
            if (event.getRightClicked() instanceof Player) {
                BingoPlayer rightClicked = GameManager.getBingoPlayer((Player) event.getRightClicked());
                if (rightClicked.getTeam() != null)
                    if (rightClicked.getTeam().equals(player.getTeam()))
                        rightClicked.stackPlayer(player);
            }
        }
    }

    @EventHandler
    public void onVehicleLeave(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            BingoPlayer player = GameManager.getBingoPlayer((Player) event.getEntity());
            if (!(event.getDismounted() instanceof Horse) && !(event.getDismounted() instanceof Pig)) {
                if (player.getGame().getGameState() != GameState.INTERMISSION || player.getGame().getGameState() != GameState.WAITING) {
                    if (player.getMountedOn() != null)
                        if (player.getMountedOn().getDebounceTill() < System.currentTimeMillis())
                            player.getMountedOn().removeStacked(player);
                } else
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        BingoPlayer player = GameManager.getBingoPlayer(event.getPlayer());
        if (player.getGame() != null)
            if (player.getGame().getGameState() == GameState.INTERMISSION || player.getGame().getGameState() == GameState.WAITING) {
                if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())
                    event.setCancelled(true);
            } else {
                if (event.getFrom().getChunk().getX() != event.getTo().getChunk().getX()
                        || event.getFrom().getChunk().getZ() != event.getTo().getChunk().getZ()) {
                    for (BingoPlayer bingoPlayer : player.getTeam().getPlayers()) {
                        Player p = bingoPlayer.getPlayer();
                        if (p.getLocation().distance(event.getPlayer().getLocation()) > 15) {
                            GlowApiProvider.setGlowIfPresent(event.getPlayer(), true, p);
                            GlowApiProvider.setGlowIfPresent(p, true, event.getPlayer());
                        } else {
                            GlowApiProvider.setGlowIfPresent(event.getPlayer(), false, p);
                            GlowApiProvider.setGlowIfPresent(p, false, event.getPlayer());
                        }
                    }
                }
            }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework) {
            if (event.getDamager().hasMetadata("nodamage"))
                event.setCancelled(true);
        } else {
            if (event.getDamager() instanceof Player)
                if (event.getEntity() instanceof Player) {
                    BingoPlayer damager = GameManager.getBingoPlayer((Player) event.getDamager());
                    BingoPlayer damaged = GameManager.getBingoPlayer((Player) event.getEntity());

                    if (damager.getTeam() == damaged.getTeam())
                        event.setCancelled(true);
                    else if (damaged.getGame() != null && damager.getGame() != null)
                        if (damaged.getGame() == damager.getGame())
                            event.setCancelled(!damaged.getGame().isPvp());
                }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        BingoPlayer player = GameManager.getBingoPlayer(event.getPlayer());
        if (player.getGame() != null)
            if (player.getTeam() != null)
                if (!(player.getTeam().getRespawns() + 1 >= player.getGame().getGameMode().getRespawns())) {
                    player.getTeam().setRespawns(player.getTeam().getRespawns() + 1);

                    if (player.getTeam().getLeader().getPlayer().getGameMode() != GameMode.SPECTATOR) {
                        event.setRespawnLocation(player.getTeam().getLeader().getPlayer().getLocation());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.getTeam().getLeader().stackPlayer(player);
                            }
                        }.runTaskLater(Main.main, 10L);
                    }
                } else {
                    player.setAlive(false);
                    player.getPlayer().setGameMode(GameMode.SPECTATOR);
                }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType().equals(Material.END_CRYSTAL))
            event.setCancelled(true);
    }

    @EventHandler
    public void clickHandler(PlayerInteractEvent event) {
        if (event.getItem() != null)
            if (event.getItem().getType() == Material.END_CRYSTAL) {
                if (event.getPlayer().getOpenInventory().getType() == InventoryType.CHEST) return;

                if (GameManager.isInGame(event.getPlayer()))
                    GameManager.getGame(event.getPlayer()).getVoting().open(event.getPlayer());
            }
    }

    private void callEvent(Player player, ItemStack itemStack) {
        if (!GameManager.isInGame(player)) return;
        Team team = GameManager.getTeam(player);
        Game game = GameManager.getGame(player);
        if (team == null || itemStack == null || player == null) return;
        team.checkItem(itemStack, player);
    }


}
