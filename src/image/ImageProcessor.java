package image;

import java.awt.*;
import java.util.ArrayList;

/**
 * Image processing class contains methods for processing images
 */
public class ImageProcessor {

    private static final double RED_CONTRAPTION = 0.2126;
    private static final double GREEN_CONTRAPTION = 0.7152;
    private static final double BLUE_CONTRAPTION = 0.0722;
    private static final int RGB_MAX = 255;
    private static final int HALF_OF = 2;
    private static final int MULTIPLIER = 2;

    /**
     * Gets the sub-images of the given image
     * @param image      the image to get the sub-images of
     * @param resolution the resolution of the sub-images
     * @return a List of the sub-images
     */
    public static ArrayList<Image> getSubImages(Image image, int resolution) {
        ArrayList<Image> subImages = new ArrayList<>();
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        int side = imageHeight / resolution;
        int numCols = imageWidth / side;
        for (int row = 0; row < resolution; row++) {
            for (int col = 0; col < numCols; col++) {
                Color[][] subPixels = new Color[side][side];
                for (int i = 0; i < side; i++) {
                    for (int j = 0; j < side; j++) {
                        int imageRow = row * side + i;
                        int imageCol = col * side + j;
                        subPixels[i][j] = image.getPixel(imageRow, imageCol);
                    }
                }
                subImages.add(new Image(subPixels, side, side));
            }
        }
        return subImages;
    }

    /**
     * Calculates the brightness of the given image
     *
     * @param image the image to calculate the brightness of
     * @return the brightness of the image
     */
    public static double calculateBrightness(Image image) {
        int length = image.getHeight();
        int width = image.getWidth();
        double sum = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                Color pixel = image.getPixel(i, j);
                if (pixel != null) {
                    sum += pixel.getRed() * RED_CONTRAPTION +
                            pixel.getGreen() * GREEN_CONTRAPTION +
                            pixel.getBlue() * BLUE_CONTRAPTION;
                }
            }
        }
        return sum / (length * width * RGB_MAX);
    }

    /**
     * Pads the image with white so that it is a power of two
     *
     * @param original the image to pad
     * @return the padded image
     */
    public static Image padImageWithWhite(Image original) {
        int originalHeight = original.getHeight();
        int originalWidth = original.getWidth();
        int newHeight = findClosestPowerTwo(originalHeight);
        int newWidth = findClosestPowerTwo(originalWidth);
        int paddingTop = (newHeight - originalHeight) / HALF_OF;
        int paddingLeft = (newWidth - originalWidth) / HALF_OF;

        Color[][] newPixels = new Color[newHeight][newWidth];
        // Fill with white
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                newPixels[i][j] = Color.WHITE;
            }
        }
        // Copy original image into the center
        for (int i = 0; i < originalHeight; i++) {
            for (int j = 0; j < originalWidth; j++) {
                newPixels[i + paddingTop][j + paddingLeft] = original.getPixel(i, j);
            }
        }
        return new Image(newPixels, newWidth, newHeight);
    }

    /**
     * Finds the closest power of two to the given value
     *
     * @param value the value to find the closest power of two to
     * @return the closest power of two to the given value
     */
    private static int findClosestPowerTwo(int value) {
        if (value <= 0) return 1;
        int power = 1;
        while (power < value) {
            power *= MULTIPLIER;
        }
        return power;
    }
}
