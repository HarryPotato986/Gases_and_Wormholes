package io.github.HarryPotato986.Gases_and_Wormholes.init;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ItemInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabInit {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gases_and_Wormholes.MODID);

    public static final RegistryObject<CreativeModeTab> GASES_AND_WORMHOLES_TAB = TABS.register("gases_and_wormholes_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gases_and_wormholes_tab"))
                    .icon(ItemInit.TEMP_BLOCK_ITEM.get()::getDefaultInstance)
                    .displayItems((displayParams, output) -> {
                        output.accept(ItemInit.TEMP_BLOCK_ITEM.get());
                        output.accept(ItemInit.BEDROCK_DUST.get());
                        output.accept(ItemInit.ATMOSPHERE_EXTRACTOR_ITEM.get());
                        output.accept(ItemInit.LIQUID_NITROGEN_BUCKET.get());
                    })
                    .build()
    );
}
