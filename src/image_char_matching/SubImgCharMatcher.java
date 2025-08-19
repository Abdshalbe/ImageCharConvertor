package image_char_matching;

import image_char_matching.RoundingStratgie.RounderFactory;
import image_char_matching.RoundingStratgie.RoundingKeyStrategy;

import java.util.*;

import static image_char_matching.CharConverter.convertToBoolArray;

/**
 * SubImgCharMatcher class
 * Responsible for matching a character to a brightness level based on its pixel density.
 */
public class SubImgCharMatcher {

    private static final double CHAR_ARR_SIZE = 256; // Each character is represented by 256
    private static final String DEFAULT = "abs";     // Default rounding strategy

    private final TreeSet<Character> mappedChars = new TreeSet<>();
    private final Map<Double, TreeSet<Character>> brightnessToCharMap = new HashMap<>();
    private final TreeMap<Double, TreeSet<Character>> brightnessToTreeMap = new TreeMap<>();
    private final TreeMap<Double, TreeSet<Character>> charTreeMap = new TreeMap<>();

    private RoundingKeyStrategy roundingKeyStrategy;
    private final RounderFactory rounderFactory;

    private double maxBright;
    private double minBright;

    /**
     * Constructor that initializes the matcher with a given character set.
     * @param charset array of characters to be considered for brightness mapping
     */
    public SubImgCharMatcher(char[] charset) {
        rounderFactory = new RounderFactory(this);
        this.roundingKeyStrategy = rounderFactory.createRounder(DEFAULT);
        for (char c : charset) {
            mappedChars.add(c);
            double brightness = calculateBrightness(c);
            addToInitialMap(brightness, c);
        }
        createBrightnessToCharMap();
    }

    /**
     * Returns the best matching character for a given brightness.
     * If multiple characters have the same brightness, the one with the lowest ASCII value is returned.
     * @param brightness the image brightness to match
     * @return best matching character
     */
    public char getCharByImageBrightness(double brightness) {
        double rounded = roundingKeyStrategy.round(brightness);
        TreeSet<Character> chars = brightnessToCharMap.get(rounded);
        if (chars != null && !chars.isEmpty()) {
            return chars.first(); // smallest ASCII value
        }
        return ' ';
    }

    /**
     * Adds a character to the brightness mapping.
     * @param c the character to be added
     */
    public void addChar(char c) {
        if (mappedChars.contains(c)) return;
        mappedChars.add(c);
        double brightness = calculateBrightness(c);
        addToInitialMap(brightness, c);
        if (brightness >= minBright && brightness <= maxBright) {
            double normalized = (brightness - minBright) / (maxBright - minBright);
            addToBrightnessToChar(normalized, c);
        } else {
            createBrightnessToCharMap();
        }
    }



    /**
     * Removes a character from the mapping.
     * @param c the character to remove
     */
    public void removeChar(char c) {
        if (!mappedChars.contains(c)) return;

        mappedChars.remove(c);
        double brightness = calculateBrightness(c);

        TreeSet<Character> charsAtBrightness = charTreeMap.get(brightness);
        if (charsAtBrightness != null) {
            charsAtBrightness.remove(c);
            if (charsAtBrightness.isEmpty()) {
                charTreeMap.remove(brightness); // Remove empty set to prevent ghost values
            }
        }
        if (brightness>=minBright && brightness<=maxBright) {
            createBrightnessToCharMap(); // Rebuild map with updated brightness values
        }else {
            double nBrightness = (brightness-minBright)/(maxBright-minBright);
            brightnessToCharMap.get(nBrightness).remove(c);
            brightnessToTreeMap.get(nBrightness).remove(c);
            if (brightnessToCharMap.get(nBrightness).isEmpty()) {
                brightnessToCharMap.remove(nBrightness);
                brightnessToTreeMap.remove(nBrightness);
            }
        }

    }


    /**
     * Sets the rounding strategy (abs, floor, ceil).
     * @param rounderName name of rounding strategy
     */
    public void setRounder(String rounderName) {
        this.roundingKeyStrategy = rounderFactory.createRounder(rounderName);
    }

    /**
     * Returns the closest lower and upper brightness keys for a given value.
     * @param value brightness value
     * @return array of [lowerKey, upperKey]
     */
    public double[] getClosestKey(double value) {
        double floor = brightnessToTreeMap.floorKey(value);
        double ceiling = brightnessToTreeMap.ceilingKey(value);
        return new double[]{floor, ceiling};
    }

    /**
     * Returns the set of currently mapped characters.
     * @return mapped character set
     */
    public TreeSet<Character> getMappedChars() {
        return new TreeSet<>(mappedChars);
    }

    /**
     * Rebuilds the brightness-to-character map using normalized brightness values.
     * Normalized brightness = (brightness - minBright) / (maxBright - minBright)
     */
    private void createBrightnessToCharMap() {
        brightnessToCharMap.clear();
        brightnessToTreeMap.clear();
        if (charTreeMap.isEmpty()) return;
        minBright = charTreeMap.firstKey();
        maxBright = charTreeMap.lastKey();
        for (Map.Entry<Double, TreeSet<Character>> entry : charTreeMap.entrySet()) {
            double normalized = (entry.getKey() - minBright) / (maxBright - minBright);
            for (char c : entry.getValue()) {
                addToBrightnessToChar(normalized, c);
            }
        }
    }


    /**
     * adds a character to the brightness-to-character map
     * @param normalized the normalized brightness value
     * @param value the character to add
     * */
    private void addToBrightnessToChar(double normalized, char value) {
        if (brightnessToCharMap.containsKey(normalized)) {
            brightnessToCharMap.get(normalized).add(value);
            brightnessToTreeMap.get(normalized).add(value);
        }else {
            TreeSet<Character> chars = new TreeSet<>();
            chars.add(value);
            brightnessToTreeMap.put(normalized, chars);
            brightnessToCharMap.put(normalized, chars);
        }
    }

    /**
     * a method that adds a character to the initial map
     * @param brightness the brightness of the character
     * @param c the character
     * */
    private void addToInitialMap(double brightness, char c) {
        if (charTreeMap.containsKey(brightness)) {
            charTreeMap.get(brightness).add(c);
        }else {
            TreeSet<Character> chars = new TreeSet<>();
            chars.add(c);
            charTreeMap.put(brightness, chars);
        }
    }

    /**
     * Calculates the brightness of a character by counting how many
     * pixels are 'on' in its 16x16 boolean matrix.
     * @param c the character
     * @return brightness value between 0.0 and 1.0
     */
    private double calculateBrightness(char c) {
        boolean[][] charArray = convertToBoolArray(c);
        double brightness = 0;
        for (boolean[] row : charArray) {
            for (boolean pixel : row) {
                if (pixel) brightness++;
            }
        }
        return brightness / CHAR_ARR_SIZE;
    }
}
