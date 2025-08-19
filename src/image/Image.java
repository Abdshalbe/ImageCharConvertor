package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class Image {

    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Image constructor.
     * */
    public Image(String filename) throws IOException {
        try {

            BufferedImage im = ImageIO.read(new File(filename));
            width = im.getWidth();
            height = im.getHeight();


            pixelArray = new Color[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    pixelArray[i][j] = new Color(im.getRGB(j, i));
                }
            }
        }catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Image constructor.
     * */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * getWidth()
     * @return the width of the image
     * */
    public int getWidth() {
        return width;
    }

    /**
     *  getHeight()
     * @return the height of the image
     * */
    public int getHeight() {
        return height;
    }
    /**
     *  getPixel()
     * @param x the x coordinate of the pixe
     * */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }
    /**
     * saveImage()
     * @param fileName the name of the file to save the image to
     * */
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
