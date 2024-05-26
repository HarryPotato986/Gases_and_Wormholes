package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class GnWIconButton extends GnWButton{
    public static final Component BLANK = Component.literal("");

    public ResourceLocation ICON_TEXTURE;
    protected int ICON_X;
    protected int ICON_Y;
    protected int ICON_WIDTH;
    protected int ICON_HEIGHT;
    protected boolean IS_ICON_BOUND;

    public static GnWIconBuilder GnWIconBuilder(OnPress pOnPress) {
        return new GnWIconBuilder(pOnPress);
    }

    protected GnWIconButton(int pX, int pY, int pWidth, int pHeight, OnPress pOnPress,
                            CreateNarration pCreateNarration, ResourceLocation texture, int textureX, int textureY,
                            ResourceLocation icon, int iconX, int iconY, int iconWidth, int iconHeight, boolean isIconBound) {
        super(pX, pY, pWidth, pHeight, BLANK, pOnPress, pCreateNarration, texture, textureX, textureY);
        this.ICON_TEXTURE = icon;
        this.ICON_X = iconX;
        this.ICON_Y = iconY;
        this.ICON_WIDTH = iconWidth;
        this.ICON_HEIGHT = iconHeight;
        this.IS_ICON_BOUND = isIconBound;
    }

    public GnWIconButton(GnWIconBuilder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.onPress,
                builder.createNarration, builder.TEXTURE, builder.TEXTURE_X, builder.TEXTURE_Y,
                builder.ICON_TEXTURE, builder.ICON_X, builder.ICON_Y, builder.ICON_WIDTH, builder.ICON_HEIGHT, builder.IS_ICON_BOUND);
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
        pGuiGraphics.blitNineSliced(TEXTURE, this.getX(), this.getY(), this.ICON_WIDTH + 4, this.ICON_HEIGHT + 4, 1, 1, 1,1, 194, 18, TEXTURE_X, this.getTextureY());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(this.ICON_TEXTURE, findCenter(this.getX(), this.getWidth(), this.ICON_WIDTH), findCenter(this.getY(), this.getHeight(), this.ICON_HEIGHT), this.ICON_X, this.ICON_Y, this.ICON_WIDTH, this.ICON_HEIGHT);

    }

    protected int findCenter(int x, int button, int icon) {
        return x + (button / 2) - (icon / 2);
    }


    @OnlyIn(Dist.CLIENT)
    public static class GnWIconBuilder {
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

        protected ResourceLocation ICON_TEXTURE = new ResourceLocation(Gases_and_Wormholes.MODID, "textures/gui/gnw_button.png");
        protected int ICON_X = 0;
        protected int ICON_Y = 0;
        protected int ICON_WIDTH = 16;
        protected int ICON_HEIGHT = 16;
        protected boolean IS_ICON_BOUND = true;

        public GnWIconBuilder(OnPress pOnPress) {
            this.onPress = pOnPress;
        }


        public GnWIconBuilder pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public GnWIconBuilder width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public GnWIconBuilder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public GnWIconBuilder bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public GnWIconBuilder tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public GnWIconBuilder createNarration(CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }


        public GnWIconBuilder texture(ResourceLocation texture) {
            this.TEXTURE = texture;
            return this;
        }

        public GnWIconBuilder textureX(int x) {
            this.TEXTURE_X = x;
            return this;
        }

        public GnWIconBuilder textureY(int y) {
            this.TEXTURE_Y = y;
            return this;
        }

        public GnWIconBuilder texturePos(int x, int y) {
            this.TEXTURE_X = x;
            this.TEXTURE_Y = y;
            return this;
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


        public GnWIconButton build() {
            return build(GnWIconButton::new);
        }

        public GnWIconButton build(java.util.function.Function<GnWIconBuilder, GnWIconButton> builder) {
            return builder.apply(this);
        }
    }
}
