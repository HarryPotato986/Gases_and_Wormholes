package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

public interface IndexableEnum<S extends Enum<S>> {

    public S getEnum(int index);

    public int getIndex(S currentState);
}
