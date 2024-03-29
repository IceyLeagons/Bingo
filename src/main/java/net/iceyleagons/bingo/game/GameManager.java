package net.iceyleagons.bingo.game;

import lombok.NonNull;
import net.iceyleagons.bingo.apis.PartyProvider;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.bingo.game.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Although most of the methods can be found in other classes ex: {@link BingoPlayer} or {@link Team} etc.
 * In order to manage the game better and keep track of everything, <b>All the game specific functions should be called by using the GameManager</b>
 * otherwise it can break everything. (Only call functions that are here!)
 *
 * @author TOTHTOMI
 */
public class GameManager {

    private static Map<Player, BingoPlayer> bingoPlayerMap = new HashMap<>();

    public static void savePlayersToDatabase() {
    }

    public static void loadPlayersFromDatabase() {
    }

    public static Game getGameById(@NonNull int id) {
        return Game.games.get(id);
    }

    public static void setTeam(Player player, Team team) {
        BingoPlayer bingoPlayer = getBingoPlayer(player);

        if (bingoPlayer.getTeam() != null) {
            bingoPlayer.getTeam().removePlayer(bingoPlayer);
        }
        if (team != null) team.addPlayer(bingoPlayer);
        bingoPlayer.setTeam(team);
    }


    public static Game createGame(int maxPlayer, int amountOfTeams, int startingAt, boolean allowVoting) {

        return new Game(new Random().nextInt(), maxPlayer, startingAt, amountOfTeams, allowVoting);
    }

    public static void joinGame(Player player, Game game, boolean randomTeam, Team choosenTeam) {
        if (game.getGameState() != GameState.WAITING) {
            player.sendMessage("The game you were trying to join has already started!");
            return;
        }
        if (game.isFull()) {
            player.sendMessage("Game is full!");
            return;
        }

        /*
        if (PartyProvider.isEnabled()) {
            List<UUID> players = PartyProvider.getParty(player).getMembers();
            players.forEach(partyMember -> {
                Player p = Bukkit.getPlayer(partyMember);
                if (!player.equals(p)) {
                    joinGame(p, game);
                }
            });
        }
         */


        BingoPlayer bingoPlayer = getBingoPlayer(player);
        bingoPlayer.savePlayerStats();
        bingoPlayer.setGame(game);

        if (randomTeam) {
            Team team = chooseRandomTeam(game, player);
            setTeam(player, team);
        } else
            setTeam(player, choosenTeam);
        game.addPlayer(bingoPlayer);
    }

    static int placeInto = 0;

    private static Team chooseRandomTeam(Game game, Player player) {
        Team team = game.getTeams().get(placeInto);

        placeInto++;
        if (placeInto >= game.getAmountOfTeams())
            placeInto = 0;
        if (team == null) return chooseRandomTeam(game, player);
        if (team.isFull()) return chooseRandomTeam(game, player);

        return team;
    }

    public static void leaveGame(Player player) {
        BingoPlayer bingoPlayer = getBingoPlayer(player);
        if (player.isInsideVehicle()) player.leaveVehicle();
        setTeam(player, null);
        bingoPlayer.getGame().removePlayer(bingoPlayer);
        bingoPlayer.setGame(null);
        bingoPlayer.loadPlayerStats();
        bingoPlayerMap.remove(player);
    }

    public static BingoPlayer getBingoPlayer(@NonNull Player player) {
        if (!bingoPlayerMap.containsKey(player)) {
            BingoPlayer bingoPlayer = new BingoPlayer(player);
            Integer id = null; // = FreezedPlayerManager.generateFromBingoPlayer(bingoPlayer); //Saving player
            if (id != null) bingoPlayer.setFreezedPlayerId(id);
            bingoPlayerMap.put(player, bingoPlayer);
        }
        return bingoPlayerMap.get(player);
    }

    public static boolean isInGame(@NonNull Player player) {
        return getBingoPlayer(player).getGame() != null;
    }

    public static Game getGame(@NonNull Player player) {
        BingoPlayer bingoPlayer = getBingoPlayer(player);
        return bingoPlayer.getGame();
    }

    public static Team getTeam(@NonNull Player player) {
        BingoPlayer bingoPlayer = getBingoPlayer(player);
        return bingoPlayer.getTeam();
    }

}
