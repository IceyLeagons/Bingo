package net.iceyleagons.bingo.game.team;

import lombok.Getter;
import net.iceyleagons.bingo.game.enums.BoardMode;
import net.iceyleagons.bingo.utils.XYCoordinate;

import static net.iceyleagons.bingo.items.MapImage.GRID_SIZE;

public class TeamProgressHandler {

    @Getter
    private final boolean[][] booleanMatrix = new boolean[GRID_SIZE][GRID_SIZE];

    public void setChecked(int x, int y, boolean value) {
        booleanMatrix[x][y] = value;
    }

    public void setChecked(XYCoordinate xyCoordinate, boolean value) {
        setChecked(xyCoordinate.getX(), xyCoordinate.getY(), value);
    }

    public boolean isChecked(int x, int y) {
        return booleanMatrix[x][y];
    }

    public boolean isChecked(XYCoordinate xyCoordinate) {
        return isChecked(xyCoordinate.getX(), xyCoordinate.getY());
    }

    public boolean checkForWin(BoardMode boardMode) {
        switch (boardMode) {
            case LINE:
                return checkLine();
            case FULL_HOUSE:
                return checkFullHouse();
            case DIAGONAL:
                return checkDiagonal();
        }

        return false;
    }

    private boolean checkDiagonal() {
        int count = 0;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; i < GRID_SIZE; i++) {
                for (int m = 0; m < GRID_SIZE; m++) {
                    if (isChecked(i + m, j + m)) {
                        if (++count == GRID_SIZE) return true;

                        continue;
                    }
                    count = 0;
                }
            }
        }
        return false;
    }

    /**
     * @return if the entire array is checked
     */
    private boolean checkFullHouse() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!isChecked(i, j)) return false;
            }
        }

        return true;
    }

    /**
     * @return if there's a line with all elements checked
     */
    private boolean checkLine() {
        int count;

        for (int i = 0; i < GRID_SIZE; i++) {

            count = 0;
            for (int j = 0; j < GRID_SIZE; j++) {
                if (isChecked(i, j)) {
                    if (++count == GRID_SIZE) return true;

                    continue;
                }
                count = 0;
            }

            count = 0;
            for (int j = 0; j < GRID_SIZE; j++) {
                if (isChecked(j, i)) {
                    if (++count == GRID_SIZE) return true;

                    continue;
                }
                count = 0;
            }
        }

        return false;
    }
}
