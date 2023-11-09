package io.github.HarryPotato986.Gases_and_Wormholes.compat;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.recipe.AtmosphereExtractorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AtmosphereExtractionRecipeCategory implements IRecipeCategory<AtmosphereExtractorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Gases_and_Wormholes.MODID, "atmosphere_extraction");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Gases_and_Wormholes.MODID, "textures/gui/atmosphere_extractor_gui.png");

    public static final RecipeType<AtmosphereExtractorRecipe> ATMOSPHERE_EXTRACTION_TYPE =
            new RecipeType<>(UID, AtmosphereExtractorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AtmosphereExtractionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.ATMOSPHERE_EXTRACTOR.get()));
    }


    @Override
    public RecipeType<AtmosphereExtractorRecipe> getRecipeType() {
        return ATMOSPHERE_EXTRACTION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Atmosphere Extractor");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AtmosphereExtractorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 11).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 59).addItemStack(recipe.getResultItem(null));
    }
}
