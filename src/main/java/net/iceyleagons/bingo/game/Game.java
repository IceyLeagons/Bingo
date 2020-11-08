package net.iceyleagons.bingo.game;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.enums.BoardMode;
import net.iceyleagons.bingo.game.enums.GameMode;
import net.iceyleagons.bingo.game.enums.GameState;
import net.iceyleagons.bingo.game.enums.GameTime;
import net.iceyleagons.bingo.game.teams.Team;
import net.iceyleagons.bingo.texture.MaterialTexture;
import net.iceyleagons.bingo.utils.voting.Voting;
import net.iceyleagons.bingo.utils.voting.VotingMenu;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
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
    private Map<MaterialTexture.Texture, Material> items;
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
    private int startAt;
    @Getter
    @Setter
    private boolean votingEnabled = true;
    @Getter
    @Setter
    private GameTime gameTime;
    @Getter
    @Setter
    private boolean pvp = false;

    public int getPlayerNumber() {
        return games.size();
    }

    public boolean isFull() {
        return getPlayerNumber() == getMaxPlayers();
    }

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
        getVoting().setVotingAllowed(allowVoting);
        games.put(id, this);
    }

    private void setTime() {
        world.setGameRule(GameRule.KEEP_INVENTORY, getGameMode().isKeepInventory());

        switch (getGameTime()) {
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
            case DAY:
            default:
                world.setTime(1000);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                break;
        }
    }


    @Getter
    @Setter
    private Voting voting;

    public void setupVoting() {
        voting = new Voting("Choose options", 27, Main.main);

        VotingMenu dayTime = new VotingMenu("Choose game time", 27, Main.main);
        dayTime.addVoteOption(Material.YELLOW_TERRACOTTA, 10, "dt_day", "Day");
        dayTime.addVoteOption(Material.BLACK_TERRACOTTA, 12, "dt_night", "Night");
        dayTime.addVoteOption(Material.ORANGE_TERRACOTTA, 14, "dt_sunset", "Sunset");
        dayTime.addVoteOption(Material.WHITE_TERRACOTTA, 16, "dt_nofixed", "NoFixed");
        voting.addSubMenu(Material.CLOCK, 10, dayTime, "Game Time");

        VotingMenu boardMode = new VotingMenu("Choose a board mode", 27, Main.main);
        boardMode.addVoteOption(Material.LEVER, 10, "bm_line", "Line");
        boardMode.addVoteOption(Material.PAINTING, 12, "bm_fullh", "Fullhouse");
        boardMode.addVoteOption(Material.COBWEB, 14, "bm_spread", "Spread");
        boardMode.addVoteOption(Material.STICK, 16, "bm_diag", "Diagonal");
        voting.addSubMenu(Material.PAPER, 13, boardMode, "Board Mode");

        VotingMenu gameMode = new VotingMenu("Choose a game mode", 27, Main.main);
        gameMode.addVoteOption(Material.OAK_SAPLING, 10, "gm_amat", "Amateur");
        gameMode.addVoteOption(Material.STONE_SWORD, 13, "gm_normal", "Normal");
        gameMode.addVoteOption(Material.DIAMOND_SWORD, 16, "gm_insanity", "Insanity");
        voting.addSubMenu(Material.BOOK, 16, gameMode, "Game Mode");
    }

    private void setGameTimeWinner(String dayTimeWinner) {
        if (dayTimeWinner != null)
            switch (dayTimeWinner) {
                case "dt_day":
                    setGameTime(GameTime.DAY);
                    break;
                case "dt_night":
                    setGameTime(GameTime.NIGHT);
                    break;
                case "dt_sunset":
                    setGameTime(GameTime.SUNSET);
                    break;
                case "dt_nofixed":
                default:
                    setGameTime(GameTime.NO_FIXED_TIME);
                    break;
            }
        else setGameTime(GameTime.NO_FIXED_TIME);
    }

    private void setBoardModeWinnder(String boardModeWinner) {
        if (boardModeWinner != null)
            switch (boardModeWinner) {
                case "bm_fullh":
                    setBoardMode(BoardMode.FULL_HOUSE);
                    break;
                case "bm_spread":
                    setBoardMode(BoardMode.SPREAD);
                    break;
                case "bm_diag":
                    setBoardMode(BoardMode.DIAGONAL);
                    break;
                case "bm_line":
                default:
                    setBoardMode(BoardMode.LINE);
                    break;
            }
        else setBoardMode(BoardMode.LINE);
    }

    private void setGameModeWinner(String gameModeWinner) {
        if (gameModeWinner != null)
            switch (gameModeWinner) {
                case "gm_amat":
                    setGameMode(GameMode.AMATEUR);
                    break;
                case "gm_insanity":
                    setGameMode(GameMode.INSANITY);
                    break;
                case "gm_normal":
                default:
                    setGameMode(GameMode.NORMAL);
                    break;
            }
        else setGameMode(GameMode.NORMAL);
    }


    public void updateModes() {
        String gameTimeWinner = voting.getWinningFrom("dt_day", "dt_night", "dt_sunset", "dt_nofixed");
        String boardModeWinner = voting.getWinningFrom("bm_line", "bm_fullh", "bm_spread", "bm_diag");
        String gameModeWinner = voting.getWinningFrom("gm_amat", "gm_normal", "gm_insanity");

        world.setGameRule(GameRule.KEEP_INVENTORY, getGameMode().isKeepInventory());

        setGameTimeWinner(gameTimeWinner);
        setBoardModeWinnder(boardModeWinner);
        setGameModeWinner(gameModeWinner);

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
                Objects.requireNonNull(player.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(Math.floor((float) (getGameMode().getTotalNumOfHearts() / players.size())) * 2);

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
            team.showSidebar();
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

    public boolean forceStartGame() {
        if (!(players.size() >= 2)) return false; //At least two people are required even when forced start.
        this.countdown = 5;
        this.startAt = 2;
        return true;
    }

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
