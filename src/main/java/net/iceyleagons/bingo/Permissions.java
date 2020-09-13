package net.iceyleagons.bingo;

import lombok.Getter;
import net.iceyleagons.bingo.utils.SubPerms;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author TOTHTOMI
 */
public enum Permissions {

    /*Collective permissions*/
    USER("user", "collective", SubPerms.USER.getSubPerms()),
    MODERATOR("moderator", "collective", SubPerms.MODERATOR.getSubPerms()),
    ADMIN("admin", "collective", SubPerms.ADMIN.getSubPerms()),

    /*Singular permissions*/
    //Game Perms
    GAME_ALL("all", "game",
           SubPerms.GAME_ALL.getSubPerms()),
    GAME_CREATE("create", "game", Optional.empty()),
    GAME_FORCEJOIN("forcejoin", "game", Optional.empty()),
    GAME_KICK("kick", "game", Optional.empty()),
    GAME_START("start", "game", Optional.empty()),
    GAME_STOP("stop", "game", Optional.empty()),
    GAME_JOIN("join", "game", Optional.empty()),
    GAME_LEAVE("leave", "game", Optional.empty()),

    //Vote Perms
    VOTE_TIME_ALL("time.all", "vote", SubPerms.TIME_ALL.getSubPerms()),
    VOTE_TIME_DAY("time.day", "vote", Optional.empty()),
    VOTE_TIME_NIGHT("time.night", "vote", Optional.empty()),
    VOTE_TIME_SUNSET("time.sunset", "vote", Optional.empty()),
    VOTE_TIME_NOFIXED("time.nofixed", "vote", Optional.empty()),

    VOTE_GAMEMODE_ALL("gamemode.all", "vote", SubPerms.GAMEMODE_ALL.getSubPerms()),
    VOTE_GAMEMODE_AMATEUR("gamemode.amateur", "vote", Optional.empty()),
    VOTE_GAMEMODE_NORMAL("gamemode.normal", "vote", Optional.empty()),
    VOTE_GAMEMODE_INSANITY("gamemode.insanity", "vote", Optional.empty()),

    VOTE_BOARDMODE_ALL("boardmode.all", "vote", SubPerms.BOARDMODE_ALL.getSubPerms()),
    VOTE_BOARDMODE_LINE("boardmode.line", "vote", Optional.empty()),
    VOTE_BOARDMODE_FULLHOUSE("boardmode.fullhouse", "vote", Optional.empty()),
    VOTE_BOARDMODE_DIAGONAL("boardmode.diagonal", "vote", Optional.empty()),
    VOTE_BOARDMODE_SPREAD("boardmode.spread", "vote", Optional.empty()),
    //Plugin perms
    BINGO_RELOAD("reload", "plugin", Optional.empty());

    @Getter
    private String permission;
    @Getter
    private String categoryName;
    @Getter
    private Optional<Permissions[]> subperms;
    private static List<Permissions> collectives = Arrays.asList(Permissions.USER,Permissions.MODERATOR,
            Permissions.ADMIN,Permissions.GAME_ALL,Permissions.VOTE_TIME_ALL,Permissions.VOTE_GAMEMODE_ALL,Permissions.VOTE_BOARDMODE_ALL);

    Permissions(String permission, String categoryName, Optional<Permissions[]> subperms) {
        this.permission = permission.toLowerCase();
        this.subperms = subperms;
        this.categoryName = categoryName != null ? categoryName : "";
        this.categoryName = this.categoryName.toLowerCase();
    }

    public static boolean hasPermission(Player player, Permissions permissions) {
        boolean value = player.hasPermission(permissions.getFullPermission());
        if (!value) {
            for (Permissions collective : collectives) {
                if (player.hasPermission(collective.getFullPermission())) {
                    List<Permissions> subPerms = getSubPerms(collective);
                    if (subPerms.contains(permissions)) return true;

                }
            }
        }
        return value;
    }

    private static List<Permissions> getSubPerms(Permissions permissions) {
        List<Permissions> permissionsList = new ArrayList<>();
        permissions.getSubperms().ifPresent(subperms -> {
            permissionsList.addAll(Arrays.asList(subperms));
        });
        return permissionsList;
    }

    public String getFullPermission() {
        return "bingo." + getCategoryName() + "." + getPermission();
    }

}
