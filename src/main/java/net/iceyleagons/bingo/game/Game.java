package net.iceyleagons.bingo.game;

import com.sun.istack.internal.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.enums.BoardMode;
import net.iceyleagons.bingo.game.enums.Difficulty;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.bingo.game.team.Team;
import net.iceyleagons.bingo.items.ItemDictionary;
import net.iceyleagons.bingo.utils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@EqualsAndHashCode
public class Game {

    private final GameManager gameManager;
    private final int maxPlayers;
    private final int playersPerTeam;
    private final int minimumPlayers;
    private final int mapSize = 512;
    private final World world;

    private final Set<Team> teams;
    private final Map<Player, Team> players = new HashMap<>();

    private final GameListener gameListener;

    @Setter
    private GameState gameState = GameState.WAITING;
    @Setter
    private BoardMode boardMode = BoardMode.LINE;

    @Nullable
    private BukkitTask lobbyCountdown = null;

    public Game(GameManager gameManager, int maxPlayers, int playersPerTeam, int minimumPlayers) {
        this.gameManager = gameManager;
        this.maxPlayers = maxPlayers;
        this.playersPerTeam = playersPerTeam;
        this.minimumPlayers = minimumPlayers;
        this.world = gameManager.getAndGenerateWorld(this);

        int amountOfTeams = maxPlayers/playersPerTeam;
        this.teams = createTeams(amountOfTeams, playersPerTeam);

        this.gameListener = new GameListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(this.gameListener, gameManager.getMain());
    }

    public void startCountdown() {
        lobbyCountdown = new BukkitRunnable(){
            int count = 40;
            @Override
            public void run() {
                if (count == 40 || count == 20 || count == 10 || count <= 5) {
                    broadcast(Main.PREFIX + "Game is starting in &e" + count + " seconds!");
                    if (count <= 5) {
                        broadcastTitle("&6" + GameUtils.getNumberIcon(count), "", 2, 16, 2);
                    }
                }
                if (count <= 0) {
                    interruptCountdown();
                    startGame();
                }

                count -= 1;
            }
        }.runTaskTimer(gameManager.getMain(), 0L, 20L);
    }

    public void interruptCountdown() {
        if (lobbyCountdown == null) return;

        this.lobbyCountdown.cancel();
    }

    public void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        players.keySet().forEach(p -> p.sendTitle(ChatColor.translateAlternateColorCodes('&', title),
                ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut));
    }

    public void startGame() {
        ItemDictionary itemDictionary = ItemDictionary.generateRandomItems(Difficulty.NORMAL); //TODO replace with voted difficulty

        players.values().forEach(t -> t.onGameStarted(itemDictionary));
    }

    public void endGame() {
        gameManager.onGameEnd(this);
    }

    public void addPlayer(Player player, Team team) {
        players.put(player, team);
        handlePlayerSizeLogic();
    }

    public void removePlayer(Player player) {
        players.remove(player);
        handlePlayerSizeLogic();
    }

    private void handlePlayerSizeLogic() {
        if (players.size() >= minimumPlayers) {
            startCountdown();
            return;
        }

        if (this.lobbyCountdown != null && !this.lobbyCountdown.isCancelled()) {
            broadcast(Main.PREFIX + "Game does not meet the minimum amount of players. Stopping countdown...");
        }

        interruptCountdown();
    }

    /**
     * Broadcasts a message for everyone without any prefix.
     * Should be used for internal notifications.
     *
     * @param message the message
     */
    public void broadcast(String message) {
        players.keySet().forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

    /**
     * Broadcasts a message for everyone as "global".
     *
     * @param message the message from {@link AsyncPlayerChatEvent#getMessage()}
     * @param player the player who sent the message
     */
    public void broadcastMessage(String message, Player player) {
        players.keySet().forEach(p -> {
            String msg = String.format("&8[&cGlobal&8] &7%s&8: &f", player.getName());

            //message is appended separately because we don't want to parse colors there.
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg) + message);
        });
    }

    /**
     * Creates the teams for the game.
     * Must be called after world has ben set!
     *
     * @param amount the amount of teams to create
     * @param playersPerTeam the maximum amount of players allowed in a team
     * @return the Set of tems.
     */
    private Set<Team> createTeams(int amount, int playersPerTeam) {
        Set<Team> teams = new HashSet<>(amount);

        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < amount; i++) {
            Location spawn = world.getHighestBlockAt(random.nextInt(mapSize - 50), random.nextInt(mapSize - 50)).getLocation().clone().add(0, 1, 0);
            teams.add(new Team(this, i + 1, playersPerTeam, spawn));
        }

        return teams;
    }
}
