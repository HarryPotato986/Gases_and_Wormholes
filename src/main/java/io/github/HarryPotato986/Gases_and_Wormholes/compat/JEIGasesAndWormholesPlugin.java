package io.github.HarryPotato986.Gases_and_Wormholes.compat;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.recipe.AtmosphereExtractorRecipe;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.AtmosphereExtractorScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIGasesAndWormholesPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Gases_and_Wormholes.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AtmosphereExtractionRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<AtmosphereExtractorRecipe> atmosphereExtractorRecipes = recipeManager.getAllRecipesFor(AtmosphereExtractorRecipe.Type.INSTANCE);
        registration.addRecipes(AtmosphereExtractionRecipeCategory.ATMOSPHERE_EXTRACTION_TYPE, atmosphereExtractorRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AtmosphereExtractorScreen.class, 60, 30, 20, 30, AtmosphereExtractionRecipeCategory.ATMOSPHERE_EXTRACTION_TYPE);
    }
}
