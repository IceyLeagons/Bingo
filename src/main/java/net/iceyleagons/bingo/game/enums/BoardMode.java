package net.iceyleagons.bingo.game.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardMode {

    LINE("Line"),
    FULL_HOUSE("Full House"),
    DIAGONAL("Diagonal");

    private final String displayName;
}
