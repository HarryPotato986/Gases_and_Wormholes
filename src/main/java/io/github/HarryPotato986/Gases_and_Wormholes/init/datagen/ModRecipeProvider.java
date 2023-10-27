package io.github.HarryPotato986.Gases_and_Wormholes.init.datagen;

import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ItemInit;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.TEMP_BLOCK.get())
                .pattern("XAX")
                .pattern("AXA")
                .pattern("XAX")
                .define('A', ItemInit.BEDROCK_DUST.get())
                .unlockedBy("has_bedrock_dust", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemInit.BEDROCK_DUST.get()).build()))
                .save(pWriter);
    }
}
