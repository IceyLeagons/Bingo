package net.iceyleagons.bingo.utils;

import lombok.SneakyThrows;
import net.iceyleagons.bingo.Main;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Resources {

    private static Font mcFont = null;
    private static BufferedImage checkMark = null;
    private static BufferedImage atlas = null;

    @SneakyThrows
    public static Font getMinecraftFont() {
        if (mcFont == null) {
            Main.instance.saveResource("mc.ttf", false);
            mcFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Main.instance.getResource("mc.ttf")));
            mcFont = mcFont.deriveFont(16F);
        }

        return mcFont;
    }

    @SneakyThrows
    public static BufferedImage getCheckmarkIcon() {
        if (checkMark == null) {
            Main.instance.saveResource("check.png", false);
            checkMark = ImageUtils.resize(ImageIO.read(Objects.requireNonNull(Main.instance.getResource("check.png"))), 16, 16);
        }

        return checkMark;
    }

    @SneakyThrows
    public static BufferedImage getItemAtlas() {
        if (atlas == null) {
            Main.instance.saveResource("atlas.png", false);
            atlas = ImageIO.read(Objects.requireNonNull(Main.instance.getResource("atlas.png")));
        }

        return atlas;
    }
}
