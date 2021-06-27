package net.iceyleagons.bingo.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameState {

    WAITING("Waiting"),
    INTERMISSION("Intermission"),
    IN_GAME("In-game"),
    RESET("Reseting");

    private final String displayName;

}
