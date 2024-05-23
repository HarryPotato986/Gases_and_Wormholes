package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GnWIconButton extends GnWButton{
    public ResourceLocation ICON_TEXTURE;
    protected int ICON_X;
    protected int ICON_Y;
    protected int ICON_WIDTH;
    protected int ICON_HEIGHT;
    protected boolean IS_ICON_BOUND;

    public static GnWIconBuilder GnWIconBuilder(Component pMessage, OnPress pOnPress) {
        return new GnWIconBuilder(pMessage, pOnPress);
    }

    protected GnWIconButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress,
                            CreateNarration pCreateNarration, ResourceLocation texture, int textureX, int textureY,
                            ResourceLocation icon, int iconX, int iconY, int iconWidth, int iconHeight, boolean isIconBound) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration, texture, textureX, textureY);
        this.ICON_TEXTURE = icon;
        this.ICON_X = iconX;
        this.ICON_Y = iconY;
        this.ICON_WIDTH = iconWidth;
        this.ICON_HEIGHT = iconHeight;
        this.IS_ICON_BOUND = isIconBound;
    }

    protected GnWIconButton(GnWIconBuilder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress,
                builder.createNarration, builder.TEXTURE, builder.TEXTURE_X, builder.TEXTURE_Y,
                builder.ICON_TEXTURE, builder.ICON_X, builder.ICON_Y, builder.ICON_WIDTH, builder.ICON_HEIGHT, builder.IS_ICON_BOUND);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        //pGuiGraphics.blit();
    }

    @OnlyIn(Dist.CLIENT)
    public static class GnWIconBuilder extends GnWBuilder {
        protected ResourceLocation ICON_TEXTURE;
        protected int ICON_X = 0;
        protected int ICON_Y = 0;
        protected int ICON_WIDTH = 16;
        protected int ICON_HEIGHT = 16;
        protected boolean IS_ICON_BOUND = true;

        public GnWIconBuilder(Component pMessage, OnPress pOnPress) {
            super(pMessage, pOnPress);
        }


        public GnWIconBuilder icon(ResourceLocation icon) {
            this.ICON_TEXTURE = icon;
            return this;
        }

        public GnWIconBuilder iconX(int x) {
            this.ICON_X = x;
            return this;
        }

        public GnWIconBuilder iconY(int y) {
            this.ICON_Y = y;
            return this;
        }

        public GnWIconBuilder iconPos(int x, int y) {
            this.ICON_X = x;
            this.ICON_Y = y;
            return this;
        }

        public GnWIconBuilder iconWidth(int width) {
            this.ICON_WIDTH = width;
            return this;
        }

        public GnWIconBuilder iconHeight(int height) {
            this.ICON_HEIGHT = height;
            return this;
        }

        public GnWIconBuilder iconSize(int width, int height) {
            this.ICON_WIDTH = width;
            this.ICON_HEIGHT = height;
            return this;
        }

        public GnWIconBuilder iconDimensions(int x, int y, int width, int height) {
            return this.iconPos(x, y).iconSize(width, height);
        }
    }
}
