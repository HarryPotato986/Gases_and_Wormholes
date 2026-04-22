package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

public enum WormholeSizeButtonStates implements IconGetter<WormholeSizeButtonStates>, ExtraInfo, IndexableEnum<WormholeSizeButtonStates>{
    ONE(0, "1x1"),
    TWO(15, "2x2"),
    THREE(30, "3x3"),
    FOUR(45, "4x4"),
    FIVE(60, "5x5"),
    SIX(75, "6x6"),
    SEVEN(90, "7x7");

    private final int iconY;
    private final String tooltipText;

    WormholeSizeButtonStates(int iconY, String message) {this.iconY = iconY; this.tooltipText = message;}


    @Override
    public int getIconY() {
        return this.iconY;
    }

    @Override
    public WormholeSizeButtonStates getNextState(WormholeSizeButtonStates currentState) {
        return switch (currentState) {
            case ONE -> TWO;
            case TWO -> THREE;
            case THREE -> FOUR;
            case FOUR -> FIVE;
            case FIVE -> SIX;
            case SIX -> SEVEN;
            case SEVEN -> ONE;
        };
    }

    @Override
    public String getExtraInfo() {
        return this.tooltipText;
    }

    @Override
    public String toString() {
        return getExtraInfo();
    }

    @Override
    public WormholeSizeButtonStates getEnum(int index) {
        return switch (index) {
            default -> ONE;
            case 1 -> TWO;
            case 2 -> THREE;
            case 3 -> FOUR;
            case 4 -> FIVE;
            case 5 -> SIX;
            case 6 -> SEVEN;
        };
    }

    @Override
    public int getIndex(WormholeSizeButtonStates currentState) {
        return switch (currentState) {
            case ONE -> 0;
            case TWO -> 1;
            case THREE -> 2;
            case FOUR -> 3;
            case FIVE -> 4;
            case SIX -> 5;
            case SEVEN -> 6;
        };
    }
}
