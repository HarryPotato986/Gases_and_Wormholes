package io.github.HarryPotato986.Gases_and_Wormholes;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.stress.BlockStressValues;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.BaseFluidType;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ModCreativeModeTabs;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
//import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkManager;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.ModFluids;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.ModFluidTypes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ModItems;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.ModBlockEntities;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.AtmosphereExtractorScreen;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.ModMenuTypes;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.WormholeGeneratorScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Gases_and_Wormholes.MODID)
public class Gases_and_Wormholes {
    public static final String MODID = "gasesandwormholes";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    //public static final LinkManager LINK_MANAGER = new LinkManager();

    public Gases_and_Wormholes(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        REGISTRATE.registerEventListeners(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModFluidTypes.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);

        modEventBus.register(ClientModEvents.class);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    //@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_LIQUID_NITROGEN.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_LIQUID_NITROGEN.get(), RenderType.translucent());
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.ATMOSPHERE_EXTRACTOR_MENU.get(), AtmosphereExtractorScreen::new);
            event.register(ModMenuTypes.WORMHOLE_GENERATOR_MENU.get(), WormholeGeneratorScreen::new);
        }

        @SubscribeEvent
        public static void onClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(((BaseFluidType) ModFluidTypes.LIQUID_NITROGEN_FLUID_TYPE.get()).getClientFluidTypeExtensions(),
                    ModFluidTypes.LIQUID_NITROGEN_FLUID_TYPE.get());

            event.registerFluidType(((BaseFluidType) ModFluidTypes.NITROGEN_GAS_FLUID_TYPE.get()).getClientFluidTypeExtensions(),
                    ModFluidTypes.NITROGEN_GAS_FLUID_TYPE.get());

            event.registerFluidType(((BaseFluidType) ModFluidTypes.OXYGEN_GAS_FLUID_TYPE.get()).getClientFluidTypeExtensions(),
                    ModFluidTypes.OXYGEN_GAS_FLUID_TYPE.get());
        }
    }
}
