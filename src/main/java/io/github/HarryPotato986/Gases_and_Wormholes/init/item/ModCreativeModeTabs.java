package io.github.HarryPotato986.Gases_and_Wormholes.init.item;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.ModFluids;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gases_and_Wormholes.MODID);

    public static final Supplier<CreativeModeTab> GASES_AND_WORMHOLES_TAB = TABS.register("gases_and_wormholes_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gases_and_wormholes_tab"))
                    .icon(() -> new ItemStack(ModBlocks.TEMP_BLOCK))
                    .displayItems((displayParams, output) -> {
                        output.accept(ModItems.TEMP_BLOCK_ITEM.get());
                        output.accept(ModItems.BEDROCK_DUST.get());
                        output.accept(ModItems.ATMOSPHERE_EXTRACTOR_ITEM.get());
                        output.accept(ModItems.WORMHOLE_GENERATOR_ITEM.get());
                        output.accept(ModFluids.LIQUID_NITROGEN_BUCKET.get());
                        output.accept(ModItems.NITROGEN_GAS_BUCKET.get());
                        output.accept(ModItems.OXYGEN_GAS_BUCKET.get());
                    }).build()
    );

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
