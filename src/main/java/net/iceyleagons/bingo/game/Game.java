package net.iceyleagons.bingo.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.bingo.game.enums.BoardMode;
import net.iceyleagons.bingo.game.enums.Difficulty;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.bingo.game.team.Team;
import net.iceyleagons.bingo.items.ItemDictionary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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

    public void startGame() {
        ItemDictionary itemDictionary = ItemDictionary.generateRandomItems(Difficulty.NORMAL); //TODO replace with voted difficulty

        players.values().forEach(t -> t.onGameStarted(itemDictionary));
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
