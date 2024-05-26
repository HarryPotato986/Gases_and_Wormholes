package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import net.minecraft.network.chat.Component;

public enum HorizontalFacingButtonStates implements MessageGetter<HorizontalFacingButtonStates>, IndexableEnum<HorizontalFacingButtonStates>{
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W");

    private final String message;

    HorizontalFacingButtonStates(String message){this.message = message;}

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Component getMessageAsComponent() {
        return Component.literal(this.message);
    }

    @Override
    public HorizontalFacingButtonStates getNextState(HorizontalFacingButtonStates currentState) {
        return switch (currentState) {
            case NORTH -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> WEST;
            case WEST -> NORTH;
        };
    }


    @Override
    public HorizontalFacingButtonStates getEnum(int index) {
        return switch (index) {
            default -> NORTH;
            case 1 -> SOUTH;
            case 2 -> EAST;
            case 3 -> WEST;
        };
    }

    @Override
    public int getIndex(HorizontalFacingButtonStates currentState) {
        return switch (currentState) {
            case NORTH -> 0;
            case SOUTH -> 1;
            case EAST -> 2;
            case WEST -> 3;
        };
    }
}
