package io.github.HarryPotato986.Gases_and_Wormholes.event;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = Gases_and_Wormholes.MODID)
public class ModBusEvents {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.ATMOSPHERE_EXTRACTOR_ENTITY.get(),
                (be, side) -> be.getItemHandler(side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.WORMHOLE_GENERATOR_ITEM_ENTITY.get(),
                (be, side) -> be.getItemHandler(side));


        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.ATMOSPHERE_EXTRACTOR_ENTITY.get(),
                (be, side) -> be.getFluidHandler(side));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.WORMHOLE_GENERATOR_FLUID_ENTITY.get(),
                (be, side) -> be.getFluidHandler(side));
    }
}
