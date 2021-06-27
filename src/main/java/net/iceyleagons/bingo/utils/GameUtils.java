package net.iceyleagons.bingo.utils;

public class GameUtils {

    private static final String[] NUMBERS = "➊ ➋ ➌ ➍ ➎ ➏ ➐ ➑ ➒ ➓".split(" ");

    public static String getNumberIcon(int number) {
        int n = number + 1;
        if (NUMBERS.length <= n) return "";

        return NUMBERS[n];
    }
}
