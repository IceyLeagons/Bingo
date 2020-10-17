package net.iceyleagons.bingo.map;

import lombok.SneakyThrows;
import net.iceyleagons.bingo.texture.MaterialTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author TOTHTOMI
 */
public class ItemIcon {
    private static BufferedImage atlas = null;

    @SneakyThrows
    public static BufferedImage getIcon(MaterialTexture.Texture materialTexture) {
        if (atlas == null)
            atlas = ImageIO.read(materialTexture.getAtlasFile());

        return atlas.getSubimage(materialTexture.getStartX(), materialTexture.getStartY(), 16, 16);
    }

}
