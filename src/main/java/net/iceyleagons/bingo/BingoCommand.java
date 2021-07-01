package net.iceyleagons.bingo;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.game.GameManager;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.icicle.annotations.Autowired;
import net.iceyleagons.icicle.annotations.commands.Alias;
import net.iceyleagons.icicle.annotations.commands.Command;
import net.iceyleagons.icicle.annotations.commands.CommandManager;
import net.iceyleagons.icicle.annotations.commands.checks.PlayerOnly;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandManager("bingo")
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class BingoCommand {

    private final GameManager gameManager;

    @Alias({"c"})
    @Command("create")
    public String onCreateCommand(int maxPlayers, int playersPerTeam, int minimumPlayers) {
        Game game = gameManager.createGame(maxPlayers, playersPerTeam, minimumPlayers);

        if (game == null) {//this should never happen, but just in case
            return "&cAn error occured when tried to create game. (This should never happen)";
        }

        return "&9Successfully created game with ID: &e" + game.getId();
    }

    @Alias({"j"})
    @PlayerOnly
    @Command("join")
    public String onJoin(String gameId, CommandSender commandSender) {
        Player player = (Player) commandSender;

        System.out.println(gameId);
        if (!gameManager.getGames().containsKey(gameId)) return "&cGame not found!";

        Game game = gameManager.getGames().get(gameId);

        if (game.isFull()) return "&cGame is full!";
        if (game.getGameState() == GameState.IN_GAME) return "&cYou cannot join this game, since it's in-progress!";
        if (game.getGameState() == GameState.RESET) return "&cGame is resetting, no players allowed!";

        gameManager.join(player, game);

        return "Joining game...";
    }
}
