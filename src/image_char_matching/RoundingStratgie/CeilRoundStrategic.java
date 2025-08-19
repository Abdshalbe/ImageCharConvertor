package image_char_matching.RoundingStratgie;

import image_char_matching.SubImgCharMatcher;

/**
 * rounds up to the closest key
 * */
public class CeilRoundStrategic implements RoundingKeyStrategy{
    private static final double FAIL_NUM = -1;
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * ctor
     * @param subImgCharMatcher the subImgCharMatcher that will be used to find the closest key
     * */
    public CeilRoundStrategic(SubImgCharMatcher subImgCharMatcher) {
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * rounds up to the closest key
     * @param value the value to round
     * @return the closest key
     * */
    @Override
    public double round(double value) {
        double [] keys = subImgCharMatcher.getClosestKey(value);
        if(keys[0]!=FAIL_NUM){
            return keys[0];
        }
        return keys[1];
    }
}
