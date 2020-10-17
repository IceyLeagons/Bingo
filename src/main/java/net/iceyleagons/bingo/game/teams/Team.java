package net.iceyleagons.bingo.game.teams;

import lombok.Getter;
import lombok.Setter;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.game.BingoPlayer;
import net.iceyleagons.bingo.game.Game;
import net.iceyleagons.bingo.game.GameManager;
import net.iceyleagons.bingo.game.GameUtils;
import net.iceyleagons.bingo.map.BingoRenderer;
import net.iceyleagons.bingo.map.MapImage;
import net.iceyleagons.bingo.texture.MaterialTexture;
import net.iceyleagons.bingo.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author TOTHTOMI
 */
public class Team {

    @Getter
    private final int teamID;
    @Getter
    @Setter
    private ChatColor teamColor;
    @Getter
    private final List<BingoPlayer> players;
    @Getter
    private final int maxPlayers;
    @Getter
    private final Game game;
    @Getter
    @Setter
    private MapImage mapImage;
    @Getter
    private final BingoRenderer bingoRenderer;
    @Getter
    private ItemStack mapItem;
    @Getter
    @Setter
    private BingoPlayer leader;
    @Getter
    @Setter
    private Location spawnLocation; //This is for the entire team

    private int checkedItems;
    @Getter
    @Setter
    private int respawns = 0;
    @Getter
    @Setter
    private String teamName;
    @Getter
    @Setter
    private boolean usingIntegerNames;

    @Getter
    private ScoreboardHandler scoreboardHandler;

    public Team(Game game, int ID, int maxPlayers) {
        this.teamID = ID;
        this.game = game;
        this.checkedItems = 0;
        this.players = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.mapImage = new MapImage(game.getItems());
        this.bingoRenderer = new BingoRenderer(getMapImage());
    }


    private void initMap() {
        mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        MapView mapView = Bukkit.createMap(getGame().getWorld());
        mapView.getRenderers().clear();
        mapView.addRenderer(getBingoRenderer());
        assert mapMeta != null;
        mapMeta.setMapView(mapView);
        mapItem.setItemMeta(mapMeta);
    }

    public void teamBroadcast(String msg) {
        final String finalMessage = ChatColor.translateAlternateColorCodes('&', Main.prefix + msg);
        players.forEach(p -> p.getPlayer().sendMessage(finalMessage));
    }

    public void teamMessage(Player player, String message) {
        String msg = String.format("§8[§aTeam§8] %s%s§8: §f%s", getTeamColor(), player.getName(), message);
        players.forEach(p -> p.getPlayer().sendMessage(msg));
    }

    public void checkItem(ItemStack itemStack, Player player) {
        if (getGame().getItems().containsValue(itemStack.getType())) {
            MapImage mapImage = getMapImage();
            MaterialTexture.Texture materialTexture = mapImage.getMaterialTexture(itemStack.getType());
            Integer[] position = mapImage.getPosition(materialTexture);
            int x = position[0];
            int y = position[1];
            if (!mapImage.isChecked(x, y)) {
                checkedItems++;
                mapImage.setChecked(x, y, true);
                bingoRenderer.update();
                String tname = isUsingIntegerNames() ? "#" + teamName : teamName;
                getGame().broadcast(String.format("&bThe &l%s &r&bteam has found an other item. Their progress is: &c%d items&b.",
                        getTeamColor() + tname, checkedItems), Optional.of(getPlayers()));
                teamBroadcast(String.format("&c&l%s &bhas found an item: %s",player.getName(), PacketUtils.TRANSLATIONS.get(itemStack)));
                GameUtils.spawnFireworks(player.getLocation());
                if (GameUtils.checkForWin(getMapImage().getCheckMatrix(), getGame().getBoardMode())) {
                    getGame().declareWinner(this);
                }
            }

        }
    }

    public void releasePlayers() {
        players.forEach(players -> players.getPlayer().setBedSpawnLocation(spawnLocation, true));
    }

    public void showSidebar() {
        if (getScoreboardHandler() == null) initScoreboard();

        getPlayers().forEach(player -> {
            Player p = player.getPlayer();
            Scoreboard scoreboard = ScoreboardLib.createScoreboard(p).setHandler(scoreboardHandler);
            scoreboard.setUpdateInterval(60L);
            scoreboard.activate();
            player.setScoreboard(scoreboard);
        });
    }

    private String calculateDirection(Player originPlayer, Player endPlayer) {
        Location origin = originPlayer.getLocation();
        Vector target = endPlayer.getLocation().toVector();
        origin.setDirection(target.subtract(origin.toVector()));
        float oYaw = origin.getYaw() + 180;
        float yaw = Math.round(oYaw / 45);

        String west = "←";
        String north_west = "↖";
        String north = "↑";
        String north_east = "↗";
        String east = "→";
        String south_east = "↘";
        String south = "↓";
        String south_west = "↙";

        switch ((int) yaw) {
            default:
            case 1:
                return west;
            case 2:
                return north_west;
            case 3:
                return north;
            case 4:
                return north_east;
            case 5:
                return east;
            case 6:
                return south_east;
            case 7:
                return south;
            case 8:
                return south_west;
        }
    }

    private void initScoreboard() {
        scoreboardHandler = new ScoreboardHandler() {
            @Override
            public String getTitle(Player player) {
                return ChatColor.translateAlternateColorCodes('&', Main.prefix);
            }

            @Override
            public List<Entry> getEntries(Player player) {
                EntryBuilder scoreboardText = new EntryBuilder();
                scoreboardText.blank()
                        .next("&8Team: ")
                        .next(" " + getTeamColor() + (isUsingIntegerNames() ? "#" + teamName : teamName))
                        .next("&8Game mode: ")
                        .next(" §c" + getGame().getBoardMode().name().replaceAll("_", " "))
                        .blank()
                        .next("&8Team members: ");
                GameManager.getBingoPlayer(player).getTeam().players.forEach(p -> scoreboardText.next((p.isAlive() ? " §a" : " §c") + " - " + p.getPlayer().getName()));
                if (!GameManager.getBingoPlayer(player).isAlive())
                    scoreboardText.blank().next("&cOUT!");
                return scoreboardText.build();
            }
        };
    }

    public boolean isFull() {
        return getMaxPlayers() == getPlayers().size();
    }

    public void giveMapItem() {
        if (mapItem == null) initMap();

        players.forEach(player -> player.getPlayer().getInventory().setItemInOffHand(mapItem));
    }


    public void addPlayer(BingoPlayer bingoPlayer) {
        players.add(bingoPlayer);

        ChatColor color = getTeamColor();
        bingoPlayer.getPlayer().setPlayerListName(String.format("%s§l[%s]§r%s %s", color, teamName, color, bingoPlayer.getPlayer().getName()));
        String tname = isUsingIntegerNames() ? "#" + teamName : teamName;
        bingoPlayer.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format("%s&bYou've joined the %s team&b.", Main.prefix, teamColor + tname)));
        bingoPlayer.getPlayer().teleport(this.spawnLocation);

        if (leader == null)
            leader = bingoPlayer;
        else
            leader.stackPlayer(bingoPlayer);
        //bingoPlayer.getPlayer().teleport(this.playerSpawnPoints[players.size()-1]);

    }

    public void removePlayer(BingoPlayer bingoPlayer) {
        bingoPlayer.getScoreboard().deactivate();
        players.remove(bingoPlayer);
    }

    /*****Static stuff*****/

    public static ChatColor[] colors = new ChatColor[]{ChatColor.AQUA, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE,
            ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
    public static int maxAmountOfAllowedTeams = colors.length;

    public static Map<Integer, Team> allocateTeamsForGame(Game game) {
        int numOfTeams = game.getAmountOfTeams();
        int numOfPlayersPerTeam = game.getMaxPlayers() / game.getAmountOfTeams();
        boolean useIntegerNames = true;
        //if (numOfTeams > colors.length) useIntegerNames = true;

        Map<Integer, Team> teams = new HashMap<>();

        for (int i = 0; i < numOfTeams; i++) {

            int id = i + 1;

            Team team = new Team(game, id, numOfPlayersPerTeam);
            if (useIntegerNames) {
                team.setTeamColor(ChatColor.YELLOW);
                team.setTeamName(String.valueOf(i + 1));
                team.setUsingIntegerNames(true);
            } else {
                team.setTeamColor(colors[i]);
                team.setTeamName(colors[i].name());
                team.setUsingIntegerNames(false);
            }

            teams.put(i + 1, team);
        }

        return teams;
    }

}
