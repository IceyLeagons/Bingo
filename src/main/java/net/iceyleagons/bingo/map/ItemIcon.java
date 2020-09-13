package net.iceyleagons.bingo.map;

import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.texture.MaterialTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author TOTHTOMI
 */
public class ItemIcon {

    private static BufferedImage atlas = null;

    static {
        try {
            atlas = ImageIO.read(Main.main.getResourceFile("items.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public static BufferedImage getIcon(MaterialTexture materialTexture) throws IOException {
        return atlas.getSubimage(materialTexture.getStartX(), materialTexture.getStartY(), 16, 16);
    }

}
