package net.iceyleagons.bingo.game.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author TOTHTOMI
 */
@RequiredArgsConstructor
public enum BoardMode {

    LINE("Get all 5 items in a vertical or horizontal line."), FULL_HOUSE("You won't get away easily! You need to unlock the entire 5x5 grid!"),
    SPREAD("I'm sick of unlocking items that are by each other, spread 'em!"), DIAGONAL("Why's everything spinning?!");


    @Getter
    @NonNull
    String description;

}
