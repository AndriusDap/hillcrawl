package com.andrius.hills.preprocesing.asc;

import com.andrius.hills.model.AscFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ImageFactory {
    private Logger logger = LogManager.getLogger();
    private static final String folder = "images";

    public Optional<File> toImage(AscFile ascFile) {
        File directory = new File(folder);
        if (!directory.exists()) {
            boolean mkdirs = directory.mkdirs();
            logger.info("{} directories {}", mkdirs ? "Created" : "Failed to create", directory);
        }
        File file = new File(folder + File.separator + ascFile.getName() + ".png");

        if (file.exists()) {
            logger.info("{} already exists as {}, returning existing value", ascFile, file);
            return Optional.of(file);
        }

        logger.info("Rendering {} into an image", ascFile);
        short[][] cells = ascFile.getCells();
        BufferedImage image = new BufferedImage(cells.length, cells[0].length, BufferedImage.TYPE_INT_RGB);

        short max = getMax(cells);
        short min = getMin(cells);

        for (int i = 0; i < cells.length; i++) {
            short[] row = cells[i];
            for (int i1 = 0; i1 < row.length; i1++) {
                image.setRGB(i1, i, toColor(row[i1], min, max));
            }
        }

        try {
            logger.info("Saving {} as {}", ascFile, file);
            ImageIO.write(image, "png", file);
            return Optional.of(file);
        } catch (IOException e) {
            logger.error("Failed to save image as {} {}", file, e.getClass());
            return Optional.empty();
        }
    }

    private short getMin(short[][] cells) {
        short min = 0;
        for (short[] cell : cells) {
            for (short i : cell) {
                if (min > i && i != -9999) {
                    min = i;
                }
            }
        }
        return min;

    }

    private short getMax(short[][] cells) {
        short max = cells[0][0];
        for (short[] cell : cells) {
            for (short i : cell) {
                if (max < i) {
                    max = i;
                }
            }
        }
        return max;
    }

    private int toColor(short i, short min, short max) {
        if (i == -9999) {
            return toRgb(100, 100, 255);
        } else {
            var value = (int) (255 * ((double) i - min) / (max - min));
            return toRgb(value, value, value);
        }
    }

    private int toRgb(int red, int green, int blue) {
        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;
        return rgb;
    }
}
