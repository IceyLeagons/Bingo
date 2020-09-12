package net.iceyleagons.bingo.game;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.enums.BoardMode;
import net.iceyleagons.bingo.game.enums.GameMode;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.bingo.game.enums.GameTime;
import net.iceyleagons.bingo.game.teams.Team;
import net.iceyleagons.bingo.game.voting.Vote;
import net.iceyleagons.bingo.game.voting.VoteMenu;
import net.iceyleagons.bingo.map.MapImage;
import net.iceyleagons.bingo.texture.MaterialTexture;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TOTHTOMI
 */

public class Game {

    public static Map<Integer, Game> games = new HashMap<>();

    @Getter
    private final int id;
    @Getter
    private final int maxPlayers;
    @Getter
    private final int amountOfTeams;
    @Getter
    @Setter
    private Map<MaterialTexture, Material> items;
    @Getter
    private final Map<Integer, Team> teams;
    @Getter
    @Setter
    private GameState gameState;
    @Getter
    @Setter
    private GameMode gameMode;
    @Getter
    @Setter
    private BoardMode boardMode;
    @Getter
    @Setter
    private List<BingoPlayer> players;
    @Getter
    @Setter
    private World world;
    @Getter
    @Setter
    private World nether;
    @Getter
    private final int startAt;
    @Getter
    @Setter
    private boolean votingEnabled = true;
    @Getter
    @Setter
    private GameTime gameTime;
    @Getter
    @Setter
    private boolean pvp = false;

    public Game(int id, int maxPlayers, int startAt, int amountOfTeams, boolean allowVoting) {
        if ((maxPlayers % amountOfTeams) != 0)
            throw new IllegalArgumentException("The remainder of maxPlayers divided by amountOfTeams should be 0!");
        if (amountOfTeams > Team.maxAmountOfAllowedTeams)
            throw new IllegalArgumentException("The maximum amount of teams/game is " + Team.maxAmountOfAllowedTeams + "!");

        this.id = id;
        this.startAt = startAt;
        setGameMode(GameMode.NORMAL);
        World[] worlds = WorldManager.allocateWorld(this);
        this.world = worlds[0];
        //this.nether = worlds[1];
        //GameUtils.pregenerate(world,-16,-16,16,16); // from -512, -512 to 512, 512
        this.items = MaterialTexture.random(getGameMode().getFree(), getGameMode().getEasy(), getGameMode().getMedium(), getGameMode().getHard(), getGameMode().getExpert());
        this.maxPlayers = maxPlayers;
        this.amountOfTeams = amountOfTeams;
        setPlayers(new ArrayList<>());
        setGameState(GameState.WAITING);
        setBoardMode(BoardMode.LINE);
        setGameTime(GameTime.NO_FIXED_TIME);
        this.teams = Team.allocateTeamsForGame(this);
        GameUtils.allocateTeamLocations(this, 512);
        setupVoting();
        getVote().setClosed(!allowVoting);
        games.put(id, this);
    }

    private void setTime() {
        world.setGameRule(GameRule.KEEP_INVENTORY, getGameMode().isKeepInventory());

        switch (getGameTime()) {
            case DAY:
                world.setTime(1000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                break;
            case NIGHT:
                world.setTime(16000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                break;
            case SUNSET:
                world.setTime(12000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                break;
            case NO_FIXED_TIME:
                world.setTime(1000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                break;
        }
    }


    @Getter
    @Setter
    private Vote vote;

    public void setupVoting() {
        vote = new Vote();

        VoteMenu dayTime = new VoteMenu(Material.ORANGE_TERRACOTTA, "GameTime", Collections.singletonList("asd"));
        dayTime.addVoteOption(Material.YELLOW_TERRACOTTA, "Day", Collections.singletonList("asd"), 1, 0);
        dayTime.addVoteOption(Material.BLACK_TERRACOTTA, "Night", Collections.singletonList("asd"), 2, 1);
        dayTime.addVoteOption(Material.ORANGE_TERRACOTTA, "Sunset", Collections.singletonList("asd"), 3, 2);
        dayTime.addVoteOption(Material.CLOCK, "NoFixed", Collections.singletonList("asd"), 4, 3);

        VoteMenu boardMode = new VoteMenu(Material.PAPER, "Board", Collections.singletonList("asd"));
        boardMode.addVoteOption(Material.STONE_BRICK_SLAB, "Line", Collections.singletonList("asd"), 1, 0);
        boardMode.addVoteOption(Material.STONE_BRICK_WALL, "Fullhouse", Collections.singletonList("asd"), 2, 1);
        boardMode.addVoteOption(Material.WHEAT_SEEDS, "Spread", Collections.singletonList("asd"), 3, 2);
        boardMode.addVoteOption(Material.STICK, "Diagonal", Collections.singletonList("asd"), 4, 3);

        VoteMenu gameMode = new VoteMenu(Material.BOOK, "GameMode", Collections.singletonList("asd"));
        gameMode.addVoteOption(Material.OAK_SAPLING, "Amateur", Collections.singletonList("asd"), 1, 0);
        gameMode.addVoteOption(Material.STONE_SWORD, "Normal", Collections.singletonList("asd"), 2, 1);
        gameMode.addVoteOption(Material.DIAMOND_SWORD, "Insanity", Collections.singletonList("asd"), 3, 2);

        vote.addMenu(dayTime, 0, 1);
        vote.addMenu(boardMode, 1, 2);
        vote.addMenu(gameMode, 2, 3);
        vote.setClosed(false);
    }

    public void updateModes() {
        int dayTimeWinning = vote.getVoteMenu(0).getIdOfWinningOption();
        int gameModeWinning = vote.getVoteMenu(2).getIdOfWinningOption();
        int boardModeWinning = vote.getVoteMenu(1).getIdOfWinningOption();

        if (dayTimeWinning == -1 || dayTimeWinning == 0) setGameTime(GameTime.DAY);
        else if (dayTimeWinning == 1) setGameTime(GameTime.NIGHT);
        else if (dayTimeWinning == 2) setGameTime(GameTime.SUNSET);
        else if (dayTimeWinning == 3) setGameTime(GameTime.NO_FIXED_TIME);

        if (gameModeWinning == -1 || gameModeWinning == 1) setGameMode(GameMode.NORMAL);
        else if (gameModeWinning == 0) setGameMode(GameMode.AMATEUR);
        else if (gameModeWinning == 2) setGameMode(GameMode.INSANITY);

        if (boardModeWinning == -1 || boardModeWinning == 0) setBoardMode(BoardMode.LINE);
        else if (boardModeWinning == 1) setBoardMode(BoardMode.FULL_HOUSE);
        else if (boardModeWinning == 2) setBoardMode(BoardMode.SPREAD);
        else if (boardModeWinning == 3) setBoardMode(BoardMode.DIAGONAL);


        this.items = MaterialTexture.random(getGameMode().getFree(), getGameMode().getEasy(), getGameMode().getMedium(), getGameMode().getHard(), getGameMode().getExpert());
        setTime();

        if (gameMode.isSmallerMap()) {
            GameUtils.allocateTeamLocations(this, 256);

            final PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, false, false);
            teams.forEach((ignored, team) -> {
                int amb = 0;
                for (BingoPlayer player : team.getPlayers()) {
                    player.getPlayer().addPotionEffect(blindness);
                    amb++;

                    if (player.getMountedOn() != null)
                        player.getMountedOn().removeStacked(player);

                    player.getPlayer().teleport(team.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            team.getLeader().stackPlayer(player);
                        }
                    }.runTaskLater(Main.main, amb * 5L);
                }
            });

            world.getWorldBorder().setSize(512);
        }

        teams.values().forEach(team -> {
            team.getMapImage().update(this.items);
            team.getBingoRenderer().update();
        });
    }

    public void globalMessage(BingoPlayer player, String message) {
        Team team = player.getTeam();
        String msg = String.format("§8[§cGlobal§8] %s%s§8: §f%s", team.getTeamColor(), player.getPlayer().getName(), message);
        players.forEach(p -> p.getPlayer().sendMessage(msg));
    }

    public void broadcast(String message, Optional<List<BingoPlayer>> ignore) {
        final String finalMessage = ChatColor.translateAlternateColorCodes('&', Main.prefix + message);
        List<BingoPlayer> ignored = ignore.orElse(Collections.emptyList());

        for (BingoPlayer p : players)
            if (!ignored.contains(p))
                p.getPlayer().sendMessage(finalMessage);
    }

    public void declareWinner(Team team) {
        String tname = team.isUsingIntegerNames() ? "#" + team.getTeamName() : team.getTeamName();
        broadcast("&bThe team " + ChatColor.BOLD + team.getTeamColor() + tname + " &r&bhas won the game!", Optional.empty());
        Bukkit.getScheduler().runTaskLater(Main.main, this::endGame, 100L);
    }


    private BukkitTask intermission;

    private void countdownOver() {
        if (getGameMode().isChangeHearts())
            for (BingoPlayer player : players)
                Objects.requireNonNull(player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(Math.floor((float) (getGameMode().getTotalNumOfHearts() / players.size())));

        if (getGameMode().getAbsorptionTime() > 0)
            for (BingoPlayer player : getPlayers())
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, getGameMode().getAbsorptionTime() * 20, 1, true, false));

        if (getGameMode().getResistanceTime() > 0)
            for (BingoPlayer player : getPlayers())
                player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, getGameMode().getResistanceTime() * 20, 0, true, false));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getGameMode().isPvp())
                    pvp = true;
                if (getGameMode().getShrinkSpeed() != 0)
                    world.getWorldBorder().setSize(0, getGameMode().getShrinkSpeed() * 300L);
            }
        }.runTaskLater(Main.main, getGameMode().getGracePeriod() * 20L);
    }

    public void startGame() {
        lobbyCountdown.cancel();
        setGameState(GameState.INTERMISSION);
        teams.values().forEach(team -> {
            team.showSidebar(true);
            team.giveMapItem();
        });

        AtomicInteger countdown = new AtomicInteger(15);
        intermission = Bukkit.getScheduler().runTaskTimer(Main.main, () -> {
            if (countdown.get() > 14 || countdown.get() < 6)
                broadcast(String.format("Releasing in %s seconds", countdown.get()), Optional.empty());

            if (countdown.get() <= 0) {
                getTeams().values().forEach(Team::releasePlayers);
                countdownOver();
                setGameState(GameState.IN_GAME);
                broadcast("The game has started! Good luck!", Optional.empty());
                stopIntermission();
            }
            countdown.getAndDecrement();
        }, 0L, 20L);
    }

    private void stopIntermission() {
        intermission.cancel();
    }


    public void endGame() {
        setGameState(GameState.RESETING);
        broadcast("Teleporting players back...", Optional.empty());
        for (int i = 0; i <= players.size(); i++) {
            GameManager.leaveGame(players.get(i).getPlayer());
            if (i == players.size() - 1) {
                players.clear();
                Bukkit.getScheduler().runTaskLater(Main.main, () -> WorldManager.deleteGameWorld(this), 20L * 10); //Deleting the world only after 10 seconds.
                break;
            }
        }
        games.remove(getId());
    }

    private BukkitTask lobbyCountdown;
    private int countdown = 40;


    private void updateCountdown() {
        if (startAt <= players.size()) {
            if (lobbyCountdown == null) {
                lobbyCountdown = Bukkit.getScheduler().runTaskTimer(Main.main, () -> {
                    if (countdown > 39 || countdown < 11)
                        broadcast(String.format("Game is starting in %d seconds...", countdown), Optional.empty());
                    if (countdown <= 0) {
                        setVotingEnabled(false);
                        updateModes();
                        broadcast(String.format("Selected board-mode &c%s", getBoardMode().name()), Optional.empty());
                        broadcast(String.format("Selected mode &c%s", getGameMode().name()), Optional.empty());
                        broadcast(String.format("Selected time &c%s", getGameTime().name()), Optional.empty());

                        startGame();
                    }
                    countdown--;
                }, 0L, 20L);
            }
        } else {
            if (lobbyCountdown != null) lobbyCountdown.cancel();
        }
    }

    public void addPlayer(BingoPlayer bingoPlayer) {
        if (gameState != GameState.WAITING) return; //Prevent joining after the game has started
        players.add(bingoPlayer);
        broadcast(String.format("&8[&a+&8] &b%s has joined the game.", bingoPlayer.getPlayer().getName()), Optional.empty());
        updateCountdown();
        //Lobby, choose teams etc
        bingoPlayer.resetPlayerStats();
    }

    public void removePlayer(BingoPlayer bingoPlayer) {
        if (getGameState() == GameState.RESETING) return; //Prevent concurrent modification exception
        broadcast(String.format("&8[&c-&8] &b%s has left the game.", bingoPlayer.getPlayer().getName()), Optional.empty());
        players.remove(bingoPlayer);
        updateCountdown();
    }

}
