package net.iceyleagons.bingo.utils;

import lombok.Getter;
import net.iceyleagons.bingo.Permissions;

import java.util.Optional;

/**
 * @author TOTHTOMI
 */
public enum SubPerms {

    USER(Optional.of(new Permissions[]{Permissions.GAME_JOIN, Permissions.GAME_LEAVE,
            Permissions.VOTE_TIME_ALL, Permissions.VOTE_BOARDMODE_ALL, Permissions.VOTE_GAMEMODE_ALL})),
    MODERATOR(Optional.of(new Permissions[]{Permissions.GAME_JOIN, Permissions.GAME_LEAVE,
            Permissions.GAME_KICK,Permissions.VOTE_TIME_ALL, Permissions.VOTE_BOARDMODE_ALL, Permissions.VOTE_GAMEMODE_ALL})),
    ADMIN(Optional.of(new Permissions[]{Permissions.GAME_ALL,
            Permissions.VOTE_TIME_ALL, Permissions.VOTE_BOARDMODE_ALL, Permissions.VOTE_GAMEMODE_ALL,Permissions.BINGO_RELOAD})),
    GAME_ALL(Optional.of(new Permissions[]{Permissions.GAME_CREATE, Permissions.GAME_FORCEJOIN, Permissions.GAME_KICK,
            Permissions.GAME_START, Permissions.GAME_STOP, Permissions.GAME_JOIN, Permissions.GAME_LEAVE})),
    TIME_ALL(Optional.of(new Permissions[]{Permissions.VOTE_TIME_DAY, Permissions.VOTE_TIME_NIGHT
            , Permissions.VOTE_TIME_SUNSET, Permissions.VOTE_TIME_NOFIXED})),
    GAMEMODE_ALL(Optional.of(new Permissions[]{Permissions.VOTE_GAMEMODE_AMATEUR,
            Permissions.VOTE_GAMEMODE_NORMAL, Permissions.VOTE_GAMEMODE_INSANITY})),
    BOARDMODE_ALL(Optional.of(new Permissions[]{Permissions.VOTE_BOARDMODE_LINE, Permissions.VOTE_BOARDMODE_FULLHOUSE,
            Permissions.VOTE_BOARDMODE_DIAGONAL, Permissions.VOTE_BOARDMODE_SPREAD}));

    @Getter
    private Optional<Permissions[]> subPerms;

    SubPerms(Optional<Permissions[]> subPerms) {

        this.subPerms = subPerms;
    }

}
