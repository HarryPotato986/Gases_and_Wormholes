package io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class GnWButton extends AbstractButton {
    public static final int SMALL_WIDTH = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 20;
    protected static final CreateNarration DEFAULT_NARRATION = Supplier::get;
    protected final OnPress onPress;
    protected final CreateNarration createNarration;

    public ResourceLocation TEXTURE;
    protected int TEXTURE_X = 0;
    protected int TEXTURE_Y = 0;

    public static GnWBuilder GnWBuilder(Component pMessage, OnPress pOnPress) {
        return new GnWBuilder(pMessage, pOnPress);
    }

    protected GnWButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress,
                        CreateNarration pCreateNarration, ResourceLocation texture, int textureX, int textureY) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.onPress = pOnPress;
        this.createNarration = pCreateNarration;
        this.TEXTURE = texture;
        this.TEXTURE_X = textureX;
        this.TEXTURE_Y = textureY;
    }

    protected GnWButton(GnWBuilder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress,
                builder.createNarration, builder.TEXTURE, builder.TEXTURE_X, builder.TEXTURE_Y);
    }

    public void onPress() {
        this.onPress.onPress(this);
    }

    protected @NotNull MutableComponent createNarrationMessage() {
        return this.createNarration.createNarrationMessage(super::createNarrationMessage);
    }

    public void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        this.defaultButtonNarrationText(pNarrationElementOutput);
    }


    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        pGuiGraphics.blitNineSliced(TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 1, 1,1, 194, 18, TEXTURE_X, this.getTextureY());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = getFGColor();
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    protected int getTextureY() {
        int i = TEXTURE_Y;
        if (!this.active) {
            i = 2;
        } else if (this.isHovered()) {
            i = 1;
        }

        return i * 18;
    }

    public void renderHint(GuiGraphics pGuiGraphics, Font font, int pMouseX, int pMouseY, String hint) {
        if(isHovered()) {
            pGuiGraphics.renderTooltip(font, Component.literal(hint), pMouseX, pMouseY);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public static class GnWBuilder {
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

        public GnWBuilder(Component pMessage, OnPress pOnPress) {
            this.message = pMessage;
            this.onPress = pOnPress;
        }

        public GnWBuilder pos(int pX, int pY) {
            this.x = pX;
            this.y = pY;
            return this;
        }

        public GnWBuilder width(int pWidth) {
            this.width = pWidth;
            return this;
        }

        public GnWBuilder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public GnWBuilder bounds(int pX, int pY, int pWidth, int pHeight) {
            return this.pos(pX, pY).size(pWidth, pHeight);
        }

        public GnWBuilder tooltip(@Nullable Tooltip pTooltip) {
            this.tooltip = pTooltip;
            return this;
        }

        public GnWBuilder createNarration(CreateNarration pCreateNarration) {
            this.createNarration = pCreateNarration;
            return this;
        }


        public GnWBuilder texture(ResourceLocation texture) {
            this.TEXTURE = texture;
            return this;
        }

        public GnWBuilder textureX(int x) {
            this.TEXTURE_X = x;
            return this;
        }

        public GnWBuilder textureY(int y) {
            this.TEXTURE_Y = y;
            return this;
        }

        public GnWBuilder texturePos(int x, int y) {
            this.TEXTURE_X = x;
            this.TEXTURE_Y = y;
            return this;
        }

        public GnWButton build() {
            return build(GnWButton::new);
        }

        public GnWButton build(Function<GnWBuilder, GnWButton> builder) {
            return builder.apply(this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface CreateNarration {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> pMessageSupplier);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(GnWButton pButton);
    }
}

