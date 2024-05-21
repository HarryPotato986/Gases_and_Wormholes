package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GnWButton extends Button {
    protected GnWButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
    }

    protected GnWButton(Builder builder) {
        super(builder);
    }


    public static class Builder extends Button.Builder {
        private ResourceLocation TEXTURE = WIDGETS_LOCATION;

        public Builder(Component pMessage, OnPress pOnPress) {
            super(pMessage, pOnPress);
        }

        public Builder texture(ResourceLocation texture) {
            this.TEXTURE = texture;
            return this;
        }
    }
}
