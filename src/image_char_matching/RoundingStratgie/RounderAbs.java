package image_char_matching.RoundingStratgie;


import image_char_matching.SubImgCharMatcher;
/**
 * Rounds the value to the closest key in the subImgCharMatcher
 * */
public class RounderAbs implements RoundingKeyStrategy{
    private static final double FAILING = -1;
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * Rounds the value to the closest key in the subImgCharMatcher
     * @param subImgCharMatcher the subImgCharMatcher to use
     * */
    public RounderAbs(SubImgCharMatcher subImgCharMatcher) {
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /***
     * rounds the value to the closest key in the subImgCharMatcher
     * @param value the value to round
     * @return the rounded value
     */
    @Override
    public double round(double value) {
        double [] keys = subImgCharMatcher.getClosestKey(value);
        if (keys[0] == FAILING) return keys[1];
        if (keys[1] == FAILING) return keys[0];
        return (value - keys[0]) <= ( keys[1] - value) ? keys[0] : keys[1];
    }
}
