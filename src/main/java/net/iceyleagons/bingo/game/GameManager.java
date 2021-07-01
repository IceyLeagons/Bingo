package net.iceyleagons.bingo.game;

import lombok.Getter;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.team.Team;
import net.iceyleagons.icicle.annotations.Autowired;
import net.iceyleagons.icicle.annotations.Service;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
public class GameManager {

    @Autowired
    private Main main;

    private final Map<String, Game> games = new HashMap<>();
    private final Map<Player, Game> playerGames = new HashMap<>();

    public Game createGame(int maxPlayers, int playersPerTeam, int minimumPlayers) {
        Game game = new Game(this, maxPlayers, playersPerTeam, minimumPlayers);
        games.put(game.getId(), game);
        System.out.println("Putting game " + game.getId());
        return game;
    }

    public void join(Player player, Game game) {
        if (game.isFull()) {
            player.sendMessage("§cGame is full!");
            return;
        }

        playerGames.put(player, game);
        getTeamForPlayer(game).joinPlayer(player);
    }

    public void leave(Player player) {
        if (playerGames.containsKey(player)) {
            playerGames.get(player).getPlayers().get(player).removePlayer(player);
            playerGames.remove(player);
        }
    }

    public void onGameEnd(Game game) {
        game.getPlayers().forEach((p, t) -> playerGames.remove(p));
        games.remove(game);
    }

    public Team getTeamForPlayer(Game game) {
        for (Team team : game.getTeams()) {
            if (team.isFull()) continue;

            return team;
        }

        return null;
    }

    public World getAndGenerateWorld(Game game) {
        String worldName = "Game-" + game.getId();

        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generateStructures(true);
        worldCreator.type(WorldType.NORMAL);

        World world = worldCreator.createWorld();
        if (world == null) throw new IllegalStateException("World null, sh!t.");

        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(1024D);

        return world;
    }
}
