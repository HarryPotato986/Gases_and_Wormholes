package io.github.HarryPotato986.Gases_and_Wormholes.networking;

import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.networking.packet.WormholeData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Handle packets FROM the client TO the Server
public class ClientPayloadHandler {

    public static void handleDataOnMain(WormholeData data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();

            if (player.distanceToSqr(data.pos().getCenter()) > 64.0) return;

            BlockPos mainPos = data.pos();
            BlockPos partnerPos = data.partnerPos();

            level.setBlock(mainPos, ModBlocks.LINKED_BLOCK.getDefaultState(), 3);
            level.setBlock(partnerPos, ModBlocks.LINKED_BLOCK.getDefaultState(), 3);

            //main wormhole
            if (level.getBlockEntity(mainPos) instanceof LinkedBlockEntity mainBE) {
                mainBE.setLinkedPartner(partnerPos);

                // Mark the block entity as changed so Minecraft saves it!
                mainBE.setChanged();
                level.sendBlockUpdated(mainPos, mainBE.getBlockState(), mainBE.getBlockState(), 3);
            }

            //partner wormhole
            if (level.getBlockEntity(partnerPos) instanceof LinkedBlockEntity partnerBE) {
                partnerBE.setLinkedPartner(mainPos);

                // Mark the block entity as changed so Minecraft saves it!
                partnerBE.setChanged();
                level.sendBlockUpdated(partnerPos, partnerBE.getBlockState(), partnerBE.getBlockState(), 3);
            }
        });
    }
}
