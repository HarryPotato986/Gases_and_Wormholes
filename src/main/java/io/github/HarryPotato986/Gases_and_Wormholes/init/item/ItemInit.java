package io.github.HarryPotato986.Gases_and_Wormholes.init.item;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Gases_and_Wormholes.MODID);

    public static final RegistryObject<Item> BEDROCK_DUST = ITEMS.register("bedrock_dust", () -> new Item(new Item.Properties()));



    public static final RegistryObject<BlockItem> TEMP_BLOCK_ITEM = ITEMS.register("temp_block", () -> new BlockItem(BlockInit.TEMP_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ATMOSPHERE_EXTRACTOR_ITEM = ITEMS.register("atmosphere_extractor_item", () -> new BlockItem(BlockInit.ATMOSPHERE_EXTRACTOR.get(), new Item.Properties()));
}
