package io.github.HarryPotato986.Gases_and_Wormholes.init.datagen;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Gases_and_Wormholes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemInit.BEDROCK_DUST);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Gases_and_Wormholes.MODID,"item/" + item.getId().getPath()));
    }
}
