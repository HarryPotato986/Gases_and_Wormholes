package io.github.HarryPotato986.Gases_and_Wormholes.init.item;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.ModFluids;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gases_and_Wormholes.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final DeferredItem<Item> BEDROCK_DUST = ITEMS.register("bedrock_dust",
            () -> new Item(new Item.Properties()));



    public static final DeferredItem<BlockItem> TEMP_BLOCK_ITEM = ITEMS.register("temp_block",
            () -> new BlockItem(ModBlocks.TEMP_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> ATMOSPHERE_EXTRACTOR_ITEM = ITEMS.register("atmosphere_extractor_item",
            () -> new BlockItem(ModBlocks.ATMOSPHERE_EXTRACTOR.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> WORMHOLE_GENERATOR_ITEM = ITEMS.register("wormhole_generator_item",
            () -> new  WormholeGeneratorBlockItem(ModBlocks.WORMHOLE_GENERATOR_HELPER.get(), new Item.Properties()));





    public static final DeferredItem<Item> NITROGEN_GAS_BUCKET = ITEMS.register("nitrogen_gas_bucket",
            () -> new GasBucketItem(ModFluids.NITROGEN_GAS.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<Item> OXYGEN_GAS_BUCKET = ITEMS.register("oxygen_gas_bucket",
            () -> new GasBucketItem(ModFluids.OXYGEN_GAS.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

}
