package io.github.HarryPotato986.Gases_and_Wormholes.event;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Gases_and_Wormholes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    /*
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TileEntitiesInit.ATMOSPHERE_EXTRACTOR_ENTITY.get(), AtmosphereExtractorRendererTest::new);
    }
     */
}
