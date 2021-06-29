package net.iceyleagons.bingo.items;

import lombok.Getter;
import net.iceyleagons.bingo.game.team.Team;
import net.iceyleagons.bingo.texture.Texture;
import net.iceyleagons.bingo.utils.ImageUtils;
import net.iceyleagons.bingo.utils.Resources;
import net.iceyleagons.bingo.utils.XYCoordinate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MapImage {

    public static final int GRID_SIZE = 5;
    private static final int SIZE = 128;

    private final ItemDictionary itemDictionary;
    private final Team team;

    private final BufferedImage[][] imageMatrix = new BufferedImage[GRID_SIZE][GRID_SIZE]; //TODO fill in matrix
    @Getter
    private final Map<Material, XYCoordinate> coordinates = new HashMap<>();


    private final Font minecraftFont;
    private final BufferedImage checkMark;

    public MapImage(ItemDictionary itemDictionary, Team team) {
        this.itemDictionary = itemDictionary;
        this.team = team;

        this.minecraftFont = Resources.getMinecraftFont();
        this.checkMark = Resources.getCheckmarkIcon();
        populateImageMatrix(itemDictionary, imageMatrix, coordinates);
    }

    public BufferedImage renderCombined() {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();

        g.drawImage(getBackground(), 0, 0, null);
        g.drawImage(renderMain(), 0, 0, null);
        g.drawImage(drawCheckMarks(95, 15, 25, GRID_SIZE, team.getTeamProgressHandler().getBooleanMatrix()), 0, 0, null);

        return image;
    }

    private BufferedImage renderMain() {
        BasicStroke small = new BasicStroke(0.3f);
        BasicStroke big = new BasicStroke(2f);

        BufferedImage content = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = content.createGraphics();
        fillInDefaultGraphics(graphics2D, small, big, minecraftFont);

        ImageUtils.drawImages(graphics2D, 95, 16, 25, GRID_SIZE, imageMatrix);
        ImageUtils.drawGrid(graphics2D, 95, 16, 25, GRID_SIZE, Color.BLACK);

        return content;
    }

    private BufferedImage drawCheckMarks(int gridSize, int x, int y, int grid, boolean[][] matrix) {
        BufferedImage checks = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = checks.createGraphics();
        int gX = 0;
        int gY = 0;
        for (int i = 0; i < gridSize; i++) {
            if ((i % ((gridSize) / grid)) == 0) {
                for (int j = 0; j < gridSize; j++) {
                    if ((j % ((gridSize) / grid)) == 0) {
                        if (matrix[gX][gY]) {
                            graphics2D.drawImage(checkMark, null, x + i + (grid / 2), y + j + (grid / 2));
                        }
                        gY++;
                    }
                }
                gY = 0;
                gX++;
            }
        }
        return checks;
    }

    private static void fillInDefaultGraphics(Graphics2D graphics2D, BasicStroke small, BasicStroke big, Font minecraftFont) {
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setStroke(big);
        graphics2D.setPaint(Color.decode("#97acc4"));
        graphics2D.fillRect(1, 1, SIZE - 2, SIZE - 2);
        graphics2D.setPaint(Color.decode("#2b5c94"));
        graphics2D.drawRect(1, 1, SIZE - 2, SIZE - 2);
        graphics2D.setStroke(small);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(big);
        graphics2D.setFont(minecraftFont);
        graphics2D.drawString("Bingo!", 38, 18);
        graphics2D.setStroke(small);
    }

    private static void populateImageMatrix(ItemDictionary itemDictionary, BufferedImage[][] imageMatrix, Map<Material, XYCoordinate> coordinates) {
        ItemStack[] itemStacks = itemDictionary.getItems();

        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[i].length; j++) {
                Material material = itemStacks[i + j].getType();

                if (Texture.textures.containsKey(material)) {
                    throw new IllegalStateException("Texture map does not contain Material (Mat. from ItemDictionary)");
                }

                imageMatrix[i][j] = Texture.textures.get(material).getImage();
                coordinates.put(material, new XYCoordinate(i, j));
            }
        }
    }

    private static BufferedImage getBackground() {
        BufferedImage background = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2Db = background.createGraphics();
        graphics2Db.setPaint(Color.decode("#97acc4"));
        graphics2Db.fillRect(1, 1, SIZE - 2, SIZE - 2);

        return background;
    }
}
