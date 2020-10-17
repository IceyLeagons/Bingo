package net.iceyleagons.bingo.map;

import lombok.SneakyThrows;
import net.iceyleagons.bingo.Main;
import net.iceyleagons.bingo.texture.MaterialTexture;
import net.iceyleagons.bingo.texture.MaterialTexture.Texture;
import org.bukkit.Material;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 */
public class MapImage {


    private Map<Texture, Material> matrixMap;
    private Map<Texture, Integer[]> textureMap;
    private Map<Material, Texture> matrixSwapped;
    public static BufferedImage checkMark = null;
    public static Font minecraftFont = null;

    @SneakyThrows
    public MapImage(Map<Texture, Material> matrixMap) {
        this.matrixMap = matrixMap;
        textureMap = new HashMap<>();
        if (minecraftFont == null) {
            Main.main.saveResource("mc.ttf", false);
            minecraftFont = Font.createFont(Font.TRUETYPE_FONT, Main.main.getResource("mc.ttf"));
            minecraftFont = minecraftFont.deriveFont(16F);
        }
        if (checkMark == null) checkMark = resize(ImageIO.read(Main.main.getResource("check.png")), 16, 16);
        this.matrixSwapped = matrixMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        init();
    }

    @SneakyThrows
    public void update(Map<Texture, Material> matrixMap) {
        this.matrixMap = matrixMap;
        this.matrixSwapped = matrixMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        init();
    }

    public static final int GRID_SIZE = 5;
    public static final int SIZE = 128;

    private static BufferedImage getBackground() {
        BufferedImage background = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2Db = background.createGraphics();
        graphics2Db.setPaint(Color.decode("#97acc4"));
        graphics2Db.fillRect(1, 1, SIZE - 2, SIZE - 2);
        return background;
    }

    public Map<Texture, Material> getItems() {
        return matrixMap;
    }

    private BufferedImage[][] matrix = new BufferedImage[GRID_SIZE][GRID_SIZE];


    public Integer[] getPosition(Texture materialTexture) {
        return textureMap.get(materialTexture);
    }

    public Texture getMaterialTexture(Material material) {
        return matrixSwapped.get(material);
    }

    private boolean[][] checkMatrix = new boolean[GRID_SIZE][GRID_SIZE];

    public boolean[][] getCheckMatrix() {
        return checkMatrix;
    }

    public void setChecked(int x, int y, boolean value) {
        checkMatrix[x][y] = value;
    }

    public boolean isChecked(int x, int y) {
        return checkMatrix[x][y];
    }

    @SneakyThrows
    public void init() {
        Texture[] text = matrixMap.keySet().toArray(new Texture[25]);
        int m = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                checkMatrix[i][j] = false;
                Texture materialTexture = text[m];
                this.textureMap.put(materialTexture, new Integer[]{i, j});
                matrix[i][j] = ItemIcon.getIcon(materialTexture);
                m++;
            }
        }
    }

    public BufferedImage create() throws IOException {
        BasicStroke small = new BasicStroke(0.3f);
        BasicStroke big = new BasicStroke(2f);

        BufferedImage content = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = content.createGraphics();
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

        createGrid(graphics2D, 95, 16, 25, GRID_SIZE, Color.BLACK, matrix);

        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.drawImage(getBackground(), 0, 0, null);
        g.drawImage(content, 0, 0, null);
        BufferedImage checks = drawCheckMarks(95, 15, 25, GRID_SIZE, checkMatrix);
        g.drawImage(checks, 0, 0, null);

        return image;
    }

    @SneakyThrows
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


    private void createGrid(Graphics2D graphics2D, int gridSize, int x, int y, int grid, Color color, BufferedImage[][] matrix) {
        drawImages(graphics2D, gridSize, x, y, grid, matrix);


        graphics2D.setColor(color);
        graphics2D.setPaint(color);
        for (int i = 0; i <= gridSize; i++) {
            if ((i % ((gridSize) / grid)) == 0) {
                graphics2D.drawLine(x, y + i, x + gridSize, y + i);
            }
        }
        for (int i = 0; i <= gridSize; i++) {
            if ((i % ((gridSize) / grid)) == 0) {
                graphics2D.drawLine(x + i, y, x + i, y + gridSize);
            }
        }
    }

    private static void drawImages(Graphics2D graphics2D, int gridSize, int x, int y, int grid, BufferedImage[][] matrix) {
        int gX = 0;
        int gY = 0;
        for (int i = 0; i < gridSize; i++) {
            if ((i % ((gridSize) / grid)) == 0) {
                for (int j = 0; j < gridSize; j++) {
                    if ((j % ((gridSize) / grid)) == 0) {
                        graphics2D.drawImage(matrix[gX][gY], null, x + i + (grid / 2), y + j + (grid / 2));
                        gY++;
                    }
                }
                gY = 0;
                gX++;
            }
        }
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    /**
     * @deprecated unused. Useless. bad.
     */
    @Deprecated
    public static void toFile(BufferedImage image) throws IOException {
        File file = new File("test.png");
        ImageIO.write(image, "png", file);
    }

}
