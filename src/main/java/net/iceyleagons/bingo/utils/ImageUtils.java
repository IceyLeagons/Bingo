package net.iceyleagons.bingo.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void drawGrid(Graphics2D graphics2D, int gridSize, int x, int y, int grid, Color color) {
        graphics2D.setColor(color);
        graphics2D.setPaint(color);

        for (int i = 0; i <= gridSize; i++) {
            if ((i % ((gridSize) / grid)) == 0) {
                graphics2D.drawLine(x, y + i, x + gridSize, y + i);
                graphics2D.drawLine(x + i, y, x + i, y + gridSize);
            }
        }
        /*
        for (int i = 0; i <= gridSize; i++) {
            if ((i % ((gridSize) / grid)) == 0) {
                graphics2D.drawLine(x + i, y, x + i, y + gridSize);
            }
        }
         */
    }

    public static void drawImages(Graphics2D graphics2D, int gridSize, int x, int y, int grid, BufferedImage[][] matrix) {
        if (matrix.length != gridSize) return;
        int gX = 0;
        int gY = 0;

        for (int i = 0; i < gridSize; i++) {
            if ((i % ((gridSize) / grid)) == 0) {
                for (int j = 0; j < gridSize; j++) {
                    if ((j % ((gridSize) / grid)) == 0) {
                        graphics2D.drawImage(matrix[gX][gY++], null, x + i + (grid / 2), y + j + (grid / 2));
                        //gY++;
                    }
                }

                gY = 0;
                gX += 1;
            }
        }
    }
}
