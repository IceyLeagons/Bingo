package net.iceyleagons.bingo;

import net.iceyleagons.bingo.map.MapImage;
import net.iceyleagons.bingo.texture.MaterialTexture;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

/**
 * @author TOTHTOMI
 */
public class Test {


    public static void main(String[] args) throws IOException {
        MapImage mapImage = new MapImage(MaterialTexture.random(5,10,7,2,1));
        ImageIO.write(mapImage.create(),"png",new File("test.png"));
    }

}
