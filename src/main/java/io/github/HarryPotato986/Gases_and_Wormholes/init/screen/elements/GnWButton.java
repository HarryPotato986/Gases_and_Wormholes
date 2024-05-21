package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;

public class GnWButton extends Button {
    public ResourceLocation TEXTURE;
    private int TEXTURE_X = 0;
    private int TEXTURE_Y = 0;

    protected GnWButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress,
                        CreateNarration pCreateNarration, ResourceLocation texture, int textureX, int textureY) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
        this.TEXTURE = texture;
        this.TEXTURE_X = textureX;
        this.TEXTURE_Y = textureY;
    }

    protected GnWButton(Builder builder) {
        this()
    }


    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        pGuiGraphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = getFGColor();
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public static class Builder extends Button.Builder {
        private final Component message;
        private final Button.OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;

        private ResourceLocation TEXTURE = WIDGETS_LOCATION;
        private int TEXTURE_X = 0;
        private int TEXTURE_Y = 0;

        public Builder(Component pMessage, OnPress pOnPress) {
            super(pMessage, pOnPress);
        }

        public Button.Builder pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public Button.Builder width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public Button.Builder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public Button.Builder bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public Button.Builder tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public Button.Builder createNarration(Button.CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }


        public Builder texture(ResourceLocation texture) {
            this.TEXTURE = texture;
            return this;
        }

        public Builder textureX(int x) {
            this.TEXTURE_X = x;
            return this;
        }

        public Builder textureY(int y) {
            this.TEXTURE_Y = y;
            return this;
        }

        public Builder texturePos(int x, int y) {
            this.TEXTURE_X = x;
            this.TEXTURE_Y = y;
            return this;
        }

        public Button build() {
            return build(GnWButton::new);
        }

        public Button build(java.util.function.Function<Button.Builder, Button> builder) {
            return builder.apply(this);
        }
    }
}
