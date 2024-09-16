package io.github.HarryPotato986.Gases_and_Wormholes.init.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.elements.*;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.renderer.FluidTankRenderer;
import io.github.HarryPotato986.Gases_and_Wormholes.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

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
    private GnWButton startButton;
    private GnWMultiStateIconButton<WormholeSizeButtonStates> wormholeSizeButton;
    private GnWMultiStateButton<HorizontalFacingButtonStates> wormhole1FacingButton;
    private GnWMultiStateButton<HorizontalFacingButtonStates> wormhole2FacingButton;

    public WormholeGeneratorScreen(WormholeGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void init() {
        this.imageWidth = 240;
        this.imageHeight = 176;
        super.init();

        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        fluidRenderer = assignFluidRenderer();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        String[] values = menu.blockEntity.getEditBoxData();

        this.X1 = createNewEditBox(x + 62, y + 18, 40, 19, Component.translatable("gasesandwormholes.editboxtext.x"));
        this.Y1 = createNewEditBox(x + 109, y + 18, 40, 19, Component.translatable("gasesandwormholes.editboxtext.y"));
        this.Z1 = createNewEditBox(x + 156, y + 18, 40, 19, Component.translatable("gasesandwormholes.editboxtext.z"));
        this.X2 = createNewEditBox(x + 62, y + 54, 40, 19, Component.translatable("gasesandwormholes.editboxtext.x"));
        this.Y2 = createNewEditBox(x + 109, y + 54, 40, 19, Component.translatable("gasesandwormholes.editboxtext.y"));
        this.Z2 = createNewEditBox(x + 156, y + 54, 40, 19, Component.translatable("gasesandwormholes.editboxtext.z"));
        X1.setValue(values[0]);
        Y1.setValue(values[1]);
        Z1.setValue(values[2]);
        X2.setValue(values[3]);
        Y2.setValue(values[4]);
        Z2.setValue(values[5]);
        X1.setHint(Component.literal("X"));
        Y1.setHint(Component.literal("Y"));
        Z1.setHint(Component.literal("Z"));
        X2.setHint(Component.literal("X"));
        Y2.setHint(Component.literal("Y"));
        Z2.setHint(Component.literal("Z"));

        this.addWidget(this.X1);
        this.addWidget(this.Y1);
        this.addWidget(this.Z1);
        this.addWidget(this.X2);
        this.addWidget(this.Y2);
        this.addWidget(this.Z2);


        int[] buttonValues = menu.blockEntity.getButtonData();
        HorizontalFacingButtonStates facing1 = HorizontalFacingButtonStates.NORTH.getEnum(buttonValues[0]);
        HorizontalFacingButtonStates facing2 = HorizontalFacingButtonStates.NORTH.getEnum(buttonValues[1]);
        WormholeSizeButtonStates size = WormholeSizeButtonStates.ONE.getEnum(buttonValues[2]);

        startButton = this.addRenderableWidget(GnWButton.GnWBuilder(Component.literal("Start"), (B) -> {
            this.onStart();
        }).bounds(x + 62, y + 82, 134, 19).build());

        wormholeSizeButton = this.addRenderableWidget(GnWMultiStateIconButton.GnWMultiStateBuilder(size, (B) -> {

        }).pos(x + 203, y + 82).iconDimensions(194, 15, 15).build());

        wormhole1FacingButton = this.addRenderableWidget(GnWMultiStateButton.GnWMultiStateBuilder(facing1, (B) -> {

        }).bounds(x + 203, y + 18, 19, 19).build());

        wormhole2FacingButton = this.addRenderableWidget(GnWMultiStateButton.GnWMultiStateBuilder(facing2, (B) -> {

        }).bounds(x + 203, y + 54, 19, 19).build());

    }

    private EditBox createNewEditBox(int x, int y, int width, int height, Component baseText) {
        char[] validChars = new char[]{'0','1','2','3','4','5','6','7','8','9'};
        EditBox box = new EditBox(this.font, x, y, width, height, baseText);
        box.setMaxLength(32500);
        box.setFilter(s -> {
            if(s.isEmpty()) {
                return true;
            }

            char[] chars = s.toCharArray();
            for(char c : chars) {
                boolean isValid = false;
                for(char validChar : validChars) {
                    if(c == validChar) {
                        isValid = true;
                        break;
                    }
                }
                if(!isValid) {
                    return false;
                }
            }
            return true;
        });
        return box;
    }

    private FluidTankRenderer assignFluidRenderer() {
        return new FluidTankRenderer(10000L, true, 16, 39);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidTooltipArea(pGuiGraphics, pMouseX, pMouseY, x, y, menu.blockEntity.getFluid(), 11, 26, fluidRenderer);
        this.wormholeSizeButton.renderInfo(pGuiGraphics, this.font, pMouseX - x, pMouseY - y, "Size: ");
        this.wormhole1FacingButton.renderHint(pGuiGraphics, this.font, pMouseX - x, pMouseY - y, "Facing");
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

        renderDustBar(guiGraphics, x + 33, y + 26);

        fluidRenderer.render(guiGraphics, x + 11, y + 26, menu.FIBlockEntity.getFluid());
    }

    private void renderDustBar(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x, y, 240, 0, 8, (int) (menu.getScaledProgress() * 39));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
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

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        if (Minecraft.getInstance().level != null) {
            pGuiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundRendered(this, pGuiGraphics));
        } else {
            this.renderDirtBackground(pGuiGraphics);
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if(hasFocus()) {
            if (pKeyCode == 256 || pKeyCode == 257 || pKeyCode == 335) {
                dropFocus();
                return false;
            }
            return getFocus().keyPressed(pKeyCode, pScanCode, pModifiers);
        }

        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else if (pKeyCode != 257 && pKeyCode != 335) {
            return false;
        } else {
            return true;
        }
    }

    private boolean hasFocus() {
        return X1.isFocused() || Y1.isFocused() || Z1.isFocused() || X2.isFocused() || Y2.isFocused() || Z2.isFocused();
    }

    private EditBox getFocus() {
        if(X1.isFocused()) {
            return X1;
        } else if(Y1.isFocused()) {
            return Y1;
        } else if(Z1.isFocused()) {
            return Z1;
        } else if(X2.isFocused()) {
            return X2;
        } else if(Y2.isFocused()) {
            return Y2;
        } else if(Z2.isFocused()) {
            return Z2;
        }
        return null;
    }

    private void dropFocus() {
        X1.setFocused(false);
        Y1.setFocused(false);
        Z1.setFocused(false);
        X2.setFocused(false);
        Y2.setFocused(false);
        Z2.setFocused(false);
    }


    @Override
    public void removed() {
        super.removed();
        HorizontalFacingButtonStates facing1 = wormhole1FacingButton.getState();
        HorizontalFacingButtonStates facing2 = wormhole2FacingButton.getState();
        WormholeSizeButtonStates size = wormholeSizeButton.getState();
        menu.blockEntity.updateScreenData(X1.getValue(), Y1.getValue(), Z1.getValue(), X2.getValue(), Y2.getValue(), Z2.getValue(),
                facing1.getIndex(facing1), facing2.getIndex(facing2), size.getIndex(size));
    }

    public void onDone() {

    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidTankRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }




    public void onStart() {
        System.out.println("Button is much work");
    }
}
