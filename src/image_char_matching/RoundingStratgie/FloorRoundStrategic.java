package image_char_matching.RoundingStratgie;

import image_char_matching.SubImgCharMatcher;


/**
 * Floor rounding strategy
 * */
public class FloorRoundStrategic implements RoundingKeyStrategy{
    private static final double FAIL_NUM = -1;
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * ctor
     * @param subImgCharMatcher subImgCharMatcher
     * */
    public FloorRoundStrategic(SubImgCharMatcher subImgCharMatcher) {
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * round value
     * @param value value
     * @return rounded value
     * */
    @Override
    public double round(double value) {
        double [] keys = subImgCharMatcher.getClosestKey(value);
        if(keys[1]!=FAIL_NUM){
            return keys[1];
        }
        return keys[0];
    }
}
