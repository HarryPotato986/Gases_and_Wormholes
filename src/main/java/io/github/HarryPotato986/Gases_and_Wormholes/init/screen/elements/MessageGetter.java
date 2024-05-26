package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import net.minecraft.network.chat.Component;

public interface MessageGetter<S extends Enum<S>> {

    public String getMessage();

    public Component getMessageAsComponent();

    public S getNextState(S currentState);
}
