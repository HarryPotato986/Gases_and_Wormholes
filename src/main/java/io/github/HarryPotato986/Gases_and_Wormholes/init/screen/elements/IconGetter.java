package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import net.minecraft.network.chat.Component;

public interface IconGetter<S extends Enum<S>> {

    public int getIconY();

    public S getNextState(S currentState);
}
