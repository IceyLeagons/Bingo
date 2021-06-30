package net.iceyleagons.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.team.Team;
import net.iceyleagons.icicle.annotations.Autowired;
import net.iceyleagons.icicle.annotations.Service;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter
@Service
@AllArgsConstructor(onConstructor__ = @Autowired)
public class GameManager {

    private final Main main;
    private final Set<Game> games = new HashSet<>();

    public Game createGame(int maxPlayers, int playersPerTeam, int minimumPlayers) {
        Game game = new Game(this, maxPlayers, playersPerTeam, minimumPlayers);
        games.add(game);

        return game;
    }

    public void onGameEnd(Game game) {
        games.remove(game);
    }

    public Team chooseRandomTeam(Player player, Game game) {
        return null;
    }

    public World getAndGenerateWorld(Game game) {
        //TODO
        return null;
    }
}
