package io.github.HarryPotato986.Gases_and_Wormholes.init.recipe;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Gases_and_Wormholes.MODID);

    public static final RegistryObject<RecipeSerializer<AtmosphereExtractorRecipe>> ATMOSPHERE_EXTRACTOR_SERIALIZER =
            SERIALIZERS.register("atmosphere_extraction", () -> AtmosphereExtractorRecipe.Serializer.INSTANCE);
}
