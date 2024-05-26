package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class GnWMultiStateButton<S extends Enum<S> & MessageGetter<S> & IndexableEnum<S>> extends GnWButton{
    private S STATE;

    public static <S extends Enum<S> & MessageGetter<S> & IndexableEnum<S>> GnWMultiStateBuilder<S> GnWMultiStateBuilder(S defaultState, OnPress pOnPress) {
        return new GnWMultiStateBuilder<>(defaultState, pOnPress);
    }

    protected GnWMultiStateButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration,
                                  ResourceLocation texture, int textureX, int textureY, S defaultState) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration, texture, textureX, textureY);
        this.STATE = defaultState;
    }

    protected GnWMultiStateButton(GnWMultiStateBuilder<S> builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress,
                builder.createNarration, builder.TEXTURE, builder.TEXTURE_X, builder.TEXTURE_Y, builder.STATE);
    }

    @Override
    public void onPress() {
        super.onPress();
        S newState = STATE.getNextState(STATE);
        this.STATE = newState;
        this.setMessage(newState.getMessageAsComponent());
    }

    public S getState() {
        return this.STATE;
    }


    @OnlyIn(Dist.CLIENT)
    public static class GnWMultiStateBuilder<S extends Enum<S> & MessageGetter<S> & IndexableEnum<S>> {
        protected final Component message;
        protected final OnPress onPress;
        @Nullable
        protected Tooltip tooltip;
        protected int x;
        protected int y;
        protected int width = 150;
        protected int height = 20;
        protected CreateNarration createNarration = GnWButton.DEFAULT_NARRATION;

        protected ResourceLocation TEXTURE = new ResourceLocation(Gases_and_Wormholes.MODID, "textures/gui/gnw_button.png");
        protected int TEXTURE_X = 0;
        protected int TEXTURE_Y = 0;

        protected S STATE;


        public GnWMultiStateBuilder(S defaultState, OnPress pOnPress) {
            this.STATE = defaultState;
            this.message = defaultState.getMessageAsComponent();
            this.onPress = pOnPress;
        }

        public GnWMultiStateBuilder<S> pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public GnWMultiStateBuilder<S> width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public GnWMultiStateBuilder<S> size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public GnWMultiStateBuilder<S> bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public GnWMultiStateBuilder<S> tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public GnWMultiStateBuilder<S> createNarration(CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }


        public GnWMultiStateBuilder<S> texture(ResourceLocation texture) {
            this.TEXTURE = texture;
            return this;
        }

        public GnWMultiStateBuilder<S> textureX(int x) {
            this.TEXTURE_X = x;
            return this;
        }

        public GnWMultiStateBuilder<S> textureY(int y) {
            this.TEXTURE_Y = y;
            return this;
        }

        public GnWMultiStateBuilder<S> texturePos(int x, int y) {
            this.TEXTURE_X = x;
            this.TEXTURE_Y = y;
            return this;
        }

        public GnWMultiStateButton<S> build() {
            return build(GnWMultiStateButton::new);
        }

        public GnWMultiStateButton<S> build(Function<GnWMultiStateBuilder<S>, GnWMultiStateButton<S>> builder) {
            return builder.apply(this);
        }
    }
}
