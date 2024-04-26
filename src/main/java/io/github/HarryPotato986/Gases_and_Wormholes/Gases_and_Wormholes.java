package io.github.HarryPotato986.Gases_and_Wormholes;

import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.CreativeTabInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.FluidInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.FluidTypesInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ItemInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.TileEntitiesInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.recipe.ModRecipes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.AtmosphereExtractorScreen;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.MenuTypesInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Gases_and_Wormholes.MODID)
public class Gases_and_Wormholes {
    public static final String MODID = "gasesandwormholes";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public Gases_and_Wormholes() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(bus);

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        TileEntitiesInit.BLOCK_ENTITIES.register(bus);
        FluidTypesInit.FLUID_TYPES.register(bus);
        FluidInit.FLUIDS.register(bus);
        CreativeTabInit.register(bus);
        MenuTypesInit.MENUS.register(bus);
        ModRecipes.SERIALIZERS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(MenuTypesInit.ATMOSPHERE_EXTRACTOR_MENU.get(), AtmosphereExtractorScreen::new);

            ItemBlockRenderTypes.setRenderLayer(FluidInit.SOURCE_LIQUID_NITROGEN.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FluidInit.FLOWING_LIQUID_NITROGEN.get(), RenderType.translucent());
        }
    }
}
