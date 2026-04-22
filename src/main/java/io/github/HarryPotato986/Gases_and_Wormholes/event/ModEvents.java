package io.github.HarryPotato986.Gases_and_Wormholes.event;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.github.HarryPotato986.Gases_and_Wormholes.networking.ClientPayloadHandler;
import io.github.HarryPotato986.Gases_and_Wormholes.networking.packet.WormholeData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Gases_and_Wormholes.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Gases_and_Wormholes.MODID).executesOn(HandlerThread.MAIN);

        registrar.playToServer(WormholeData.TYPE, WormholeData.STREAM_CODEC, ClientPayloadHandler::handleDataOnMain);
    }
}
