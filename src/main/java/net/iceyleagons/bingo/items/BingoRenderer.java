package net.iceyleagons.bingo.items;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class BingoRenderer extends MapRenderer {

    private final MapImage mapImage;
    private BufferedImage bingoImage;

    public BingoRenderer(MapImage mapImage) {
        this.mapImage = mapImage;
        update();
    }

    public void update() {
        this.bingoImage = mapImage.renderCombined();
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (bingoImage == null) return;

        canvas.drawImage(0, 0, bingoImage);
        bingoImage = null;
    }
}