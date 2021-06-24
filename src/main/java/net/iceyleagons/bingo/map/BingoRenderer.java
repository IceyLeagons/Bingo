package net.iceyleagons.bingo.map;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

/**
 * @author TOTHTOMI
 */
public class BingoRenderer extends MapRenderer {

    @Getter
    private MapImage mapImage;
    @Getter
    private BufferedImage bingoImage;

    @SneakyThrows
    public BingoRenderer(MapImage mapImage) {
        //System.out.println("BingoRenderer#newInstance(MapImage)");

        this.mapImage = mapImage;
        this.bingoImage = mapImage.create();
    }

    @SneakyThrows
    public void update() {
        bingoImage = mapImage.create();
    }
    

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (getBingoImage() == null) return;
        mapCanvas.drawImage(0, 0, getBingoImage());
        bingoImage = null;
    }
}
