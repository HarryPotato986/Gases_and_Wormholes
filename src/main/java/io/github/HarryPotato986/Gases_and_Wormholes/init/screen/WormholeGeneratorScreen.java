package io.github.HarryPotato986.Gases_and_Wormholes.init.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.renderer.FluidTankRenderer;
import io.github.HarryPotato986.Gases_and_Wormholes.util.MouseUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;

import java.util.Optional;

public class WormholeGeneratorScreen extends AbstractContainerScreen<WormholeGeneratorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Gases_and_Wormholes.MODID, "textures/gui/wormhole_generator_gui.png");

    private FluidTankRenderer fluidRenderer;
    private EditBox X1;
    private EditBox Y1;
    private EditBox Z1;
    private EditBox X2;
    private EditBox Y2;
    private EditBox Z2;

    public WormholeGeneratorScreen(WormholeGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        fluidRenderer = assignFluidRenderer();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.X1 = createNewEditBox(x + 16, y + 32, 40, 20, Component.translatable("gasesandwormholes.editboxtext.x"));
        this.Y1 = createNewEditBox(x + 68, y + 32, 40, 20, Component.translatable("gasesandwormholes.editboxtext.y"));
        this.Z1 = createNewEditBox(x + 120, y + 32, 40, 20, Component.translatable("gasesandwormholes.editboxtext.z"));
        this.X2 = createNewEditBox(x + 16, y + 68, 40, 20, Component.translatable("gasesandwormholes.editboxtext.x"));
        this.Y2 = createNewEditBox(x + 68, y + 68, 40, 20, Component.translatable("gasesandwormholes.editboxtext.y"));
        this.Z2 = createNewEditBox(x + 120, y + 68, 40, 20, Component.translatable("gasesandwormholes.editboxtext.z"));
        this.addWidget(this.X1);
        this.addWidget(this.Y1);
        this.addWidget(this.Z1);
        this.addWidget(this.X2);
        this.addWidget(this.Y2);
        this.addWidget(this.Z2);


    }

    private EditBox createNewEditBox(int x, int y, int width, int height, Component baseText) {
        EditBox box = new EditBox(this.font, x, y, width, height, baseText);
        box.setMaxLength(32500);
        return box;
    }

    private FluidTankRenderer assignFluidRenderer() {
        return new FluidTankRenderer(10000, true, 16, 39);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidTooltipArea(pGuiGraphics, pMouseX, pMouseY, x, y, menu.blockEntity.getFluid(), 26, 11, fluidRenderer);
    }

    private void renderFluidTooltipArea(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int x, int y,
                                        FluidStack stack, int offsetX, int offsetY, FluidTankRenderer renderer) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            pGuiGraphics.renderTooltip(this.font, renderer.getTooltip(stack, TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        fluidRenderer.render(guiGraphics, x + 134, y + 11, menu.blockEntity.getFluid());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.X1.render(guiGraphics, mouseX, mouseY, delta);
        this.Y1.render(guiGraphics, mouseX, mouseY, delta);
        this.Z1.render(guiGraphics, mouseX, mouseY, delta);
        this.X2.render(guiGraphics, mouseX, mouseY, delta);
        this.Y2.render(guiGraphics, mouseX, mouseY, delta);
        this.Z2.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidTankRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
}
