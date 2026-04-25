package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GnWMultiStateIconButton<S extends Enum<S> & IconGetter<S> & IndexableEnum<S>> extends GnWIconButton{
    private S STATE;

    public static <S extends Enum<S> & IconGetter<S> & IndexableEnum<S>> GnWMultiStateIconBuilder<S> GnWMultiStateIconBuilder(S defaultState, OnPress pOnPress) {
        return new GnWMultiStateIconBuilder<>(defaultState, pOnPress);
    }

    protected GnWMultiStateIconButton(int pX, int pY, int pWidth, int pHeight, OnPress pOnPress, CreateNarration pCreateNarration,
                                      ResourceLocation texture, ResourceLocation textureDisabled,
                                      ResourceLocation textureHighlighted, int textureX, int textureY,
                                      ResourceLocation icon, int iconX, int iconY, int iconWidth, int iconHeight, boolean isIconBound,
                                      S defaultState) {
        super(pX, pY, pWidth, pHeight, pOnPress, pCreateNarration, texture, textureDisabled, textureHighlighted, textureX, textureY, icon, iconX, iconY, iconWidth, iconHeight, isIconBound);
        this.STATE = defaultState;
        this.ICON_Y = defaultState.getIconY();
    }

    public GnWMultiStateIconButton(GnWMultiStateIconBuilder<S> builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.onPress,
                builder.createNarration, builder.TEXTURE, builder.TEXTURE_DISABLED, builder.TEXTURE_HIGHLIGHTED, builder.TEXTURE_X, builder.TEXTURE_Y,
                builder.ICON_TEXTURE, builder.ICON_X, builder.ICON_Y, builder.ICON_WIDTH, builder.ICON_HEIGHT, builder.IS_ICON_BOUND,
                builder.STATE);
    }

    public void renderInfo(GuiGraphics pGuiGraphics, Font font, int pMouseX, int pMouseY) {
        this.renderInfo(pGuiGraphics, font, pMouseX, pMouseY, "");
    }

    public void renderInfo(GuiGraphics pGuiGraphics, Font font, int pMouseX, int pMouseY, String prefix) {
        if(this.STATE instanceof ExtraInfo state) {
            if(isHovered()) {
                pGuiGraphics.renderTooltip(font, Component.literal(prefix + state.getExtraInfo()), pMouseX, pMouseY);
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        if(this.IS_ICON_BOUND) {
            this.setWidth(this.ICON_WIDTH + 4);
            this.setHeight(this.ICON_HEIGHT + 4);
        }
        pGuiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.ICON_WIDTH + 4, this.ICON_HEIGHT + 4);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(this.ICON_TEXTURE, findCenter(this.getX(), this.getWidth(), this.ICON_WIDTH), findCenter(this.getY(), this.getHeight(), this.ICON_HEIGHT), 0, this.ICON_X, this.ICON_Y, this.ICON_WIDTH, this.ICON_HEIGHT, 15, 105);

    }

    @Override
    public void onPress() {
        super.onPress();
        S newState = STATE.getNextState(STATE);
        this.STATE = newState;
        this.ICON_Y = newState.getIconY();
    }

    public S getState() {
        return this.STATE;
    }


    @OnlyIn(Dist.CLIENT)
    public static class GnWMultiStateIconBuilder<S extends Enum<S> & IconGetter<S> & IndexableEnum<S>> {
        protected final OnPress onPress;
        @Nullable
        protected Tooltip tooltip;
        protected int x;
        protected int y;
        protected int width = 150;
        protected int height = 20;
        protected CreateNarration createNarration = GnWButton.DEFAULT_NARRATION;

        protected ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Gases_and_Wormholes.MODID, "buttons/gnw_button");
        protected ResourceLocation TEXTURE_DISABLED = ResourceLocation.fromNamespaceAndPath(Gases_and_Wormholes.MODID, "buttons/gnw_button_disabled");
        protected ResourceLocation TEXTURE_HIGHLIGHTED = ResourceLocation.fromNamespaceAndPath(Gases_and_Wormholes.MODID, "buttons/gnw_button_highlighted");
        protected int TEXTURE_X = 0;
        protected int TEXTURE_Y = 0;

        protected ResourceLocation ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Gases_and_Wormholes.MODID, "textures/gui/wormhole_button_icons.png");
        protected int ICON_X = 0;
        protected int ICON_Y = 0;
        protected int ICON_WIDTH = 15;
        protected int ICON_HEIGHT = 15;
        protected boolean IS_ICON_BOUND = true;

        protected S STATE;

        public GnWMultiStateIconBuilder(S defaultState, OnPress pOnPress) {
            this.STATE = defaultState;
            this.ICON_Y = defaultState.getIconY();
            this.onPress = pOnPress;
        }


        public GnWMultiStateIconBuilder<S> pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public GnWMultiStateIconBuilder<S> width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public GnWMultiStateIconBuilder<S> size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public GnWMultiStateIconBuilder<S> bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public GnWMultiStateIconBuilder<S> tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public GnWMultiStateIconBuilder<S> createNarration(CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }


        public GnWMultiStateIconBuilder<S> texture(ResourceLocation texture) {
            this.TEXTURE = texture;
            return this;
        }

        public GnWMultiStateIconBuilder<S> textureX(int x) {
            this.TEXTURE_X = x;
            return this;
        }

        public GnWMultiStateIconBuilder<S> textureY(int y) {
            this.TEXTURE_Y = y;
            return this;
        }

        public GnWMultiStateIconBuilder<S> texturePos(int x, int y) {
            this.TEXTURE_X = x;
            this.TEXTURE_Y = y;
            return this;
        }


        public GnWMultiStateIconBuilder<S> icon(ResourceLocation icon) {
            this.ICON_TEXTURE = icon;
            return this;
        }

        public GnWMultiStateIconBuilder<S> iconX(int x) {
            this.ICON_X = x;
            return this;
        }

        public GnWMultiStateIconBuilder<S> iconY(int y) {
            this.ICON_Y = y;
            return this;
        }

        public GnWMultiStateIconBuilder<S> iconWidth(int width) {
            this.ICON_WIDTH = width;
            return this;
        }

        public GnWMultiStateIconBuilder<S> iconHeight(int height) {
            this.ICON_HEIGHT = height;
            return this;
        }

        public GnWMultiStateIconBuilder<S> iconSize(int width, int height) {
            this.ICON_WIDTH = width;
            this.ICON_HEIGHT = height;
            return this;
        }

        public GnWMultiStateIconBuilder<S> iconDimensions(int x, int y, int width, int height) {
            return this.iconX(x).iconY(y).iconSize(width, height);
        }


        public GnWMultiStateIconButton<S> build() {
            return build(GnWMultiStateIconButton::new);
        }

        public GnWMultiStateIconButton<S> build(java.util.function.Function<GnWMultiStateIconBuilder<S>, GnWMultiStateIconButton<S>> builder) {
            return builder.apply(this);
        }
    }
}
