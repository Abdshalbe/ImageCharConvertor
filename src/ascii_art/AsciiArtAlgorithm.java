package ascii_art;

import ascii_output.AsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * AsciiArtAlgorithm class
 * this class mainly.
 * */
public class AsciiArtAlgorithm {

    private static final int MIN_CHAR = 32;
    private static final int MAX_CHAR = 127;
    private final SubImgCharMatcher subImageCharMatcher;
    private Image image = null;
    private AsciiOutput renderer;
    private int resolution;
    private final HashMap<Image,Double> valueSaver = new HashMap<>();


    /**
     * A constructor for AsciiArtAlgorithm class
     * @param imagePath  the path of the image that we want to convert it to ascii art
     * @param charSet    the char set that we want to use to convert the image to ascii art
     * @param resolution the resolution of the ascii art that we want to get
     * */
    public AsciiArtAlgorithm(String imagePath, char[] charSet, int resolution) {
        try {
            this.image = new Image(imagePath);
        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }
        this.renderer = OutputFactory.createOutput("console");
        this.subImageCharMatcher = new SubImgCharMatcher(charSet);
        this.resolution = resolution;
    }

    /**
     * run method
     * A method that converts the image to ascii art
     * @return the ascii art
     * */
    public char[][] run() {
        Image paddedImage = ImageProcessor.padImageWithWhite(image);
        int squareSize = paddedImage.getHeight() / resolution;
        int numCols = paddedImage.getWidth() / squareSize;
        char[][] asciiArt = new char[resolution][numCols];
        ArrayList<Image> subImages = ImageProcessor.getSubImages(paddedImage, resolution);

        for (int i = 0; i < subImages.size(); i++) {
            Image subImg = subImages.get(i);
            double brightness;
            if (!valueSaver.containsKey(subImg)) {
                brightness = ImageProcessor.calculateBrightness(subImg);
                valueSaver.put(subImg, brightness);
            } else {
                brightness = valueSaver.get(subImg);
            }
            int row = i / numCols;
            int col = i % numCols;
            asciiArt[row][col] = subImageCharMatcher.getCharByImageBrightness(brightness);
        }
        return asciiArt;
    }

    /**
     * setRounder method
     * @param rounder the rounder that we want to use to round the brightness of the image
     * */
    public void setRounder(String rounder){
        subImageCharMatcher.setRounder(rounder);
    }

    /**
     * changeResolution method
     * responsible for changing the resolution of the ascii art
     * */
    public boolean changeResolution(float direction) {
        int minRes = Math.max(1, image.getWidth()/image.getHeight()) ;
        int maxRes = Math.min(image.getHeight(), image.getWidth());
        int newRes = (int) (resolution * direction);
        if (newRes >= minRes && newRes <= maxRes) {
            resolution = newRes;
            return true;
        }
        return false;
    }

    /**
     * getCharSet method
     * @return the char set that we used to convert the image to ascii art
     * */
    public char[] getCharSet() {
        Set<Character> mappedChars = subImageCharMatcher.getMappedChars();
        char[] charSet = new char[mappedChars.size()];
        int i = 0;
        for (char c : mappedChars) {
            charSet[i++] = c;
        }
        return charSet;
    }


    /**
     * a method that adds a char to the char set
     * @param c the char that we want to add
     * */
    public void addChar(char c) {
        if ((int)c > MAX_CHAR || (int)c < MIN_CHAR){
          return;
        }
        subImageCharMatcher.addChar(c);
    }

    /**
     * removeChar method
     * responsible for removing a char from the char set
     * @param c the char that we want to remove
     * */
    public void removeChar(char c) {
        subImageCharMatcher.removeChar(c);
    }

    /**
     * getResolution method
     * @return the resolution of the ascii art
     * */
    public int getResolution() {
        return resolution;
    }
    /**
     * SetRenderer method
     * set the renderer
     * @param value the renderer that we want to use
     *                it can be "html" or "console"
     * */
    public boolean setRenderer(String value) {
        AsciiOutput asciiOutput = value.startsWith("html")?OutputFactory.createOutput("html"):null;
        asciiOutput = value.startsWith("console")?OutputFactory.createOutput("console"):asciiOutput;
        if (asciiOutput != null) {
            this.renderer = asciiOutput;
            return true;
        }
        return false;
    }
    /**
     * A method that returns the renderer
     * */
    public void apply() {
        renderer.out(run());
    }

}
