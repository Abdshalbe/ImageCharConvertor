package image_char_matching.RoundingStratgie;

import image_char_matching.SubImgCharMatcher;

/**
 *  factory for rounding strategy
 * */
public class RounderFactory {
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * create rounding strategy factory
     * @param subImgCharMatcher sub image char matcher
     * */
    public RounderFactory(SubImgCharMatcher subImgCharMatcher) {
        this.subImgCharMatcher = subImgCharMatcher;
    }
    /**
     * create rounding strategy
     * @param roundingStrategy name of strategic
     * @return  rounding strategy
     * */
    public RoundingKeyStrategy createRounder(String roundingStrategy) {
        return switch (roundingStrategy) {
            case "upper" -> new FloorRoundStrategic(subImgCharMatcher);
            case "lower" -> new CeilRoundStrategic(subImgCharMatcher);
            case "abs" -> new RounderAbs(subImgCharMatcher);
            default -> null;
        };
    }
}
