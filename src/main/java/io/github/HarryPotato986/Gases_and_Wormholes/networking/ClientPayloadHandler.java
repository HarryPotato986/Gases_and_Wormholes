package io.github.HarryPotato986.Gases_and_Wormholes.networking;

import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.networking.packet.WormholeData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.neoforge.common.world.chunk.ForcedChunkManager;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.FACING;


// Handle packets FROM the client TO the Server
public class ClientPayloadHandler {

    public static void handleDataOnMain(WormholeData data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            ServerLevel serverLevel = (ServerLevel) level;

            if (player.distanceToSqr(data.packetOrigin().getCenter()) > 64.0) return;

            BlockPos mainPos = data.pos();
            BlockPos partnerPos = data.partnerPos();
            Direction facing = data.facing();
            Direction partnerFacing = data.partnerFacing();
            int wormholeSize = data.size();

            Map<ChunkPos, List<wormholePlacementData>> blocksByChunk = new HashMap<>();

            for (int i = 0; i < wormholeSize; i++) {
                for (int j = 0; j < wormholeSize; j++) {
                    BlockPos pos = new BlockPos(mainPos.relative(facing.getCounterClockWise(), j).relative(Direction.UP, i));
                    BlockPos pos2 = new BlockPos(partnerPos.relative(partnerFacing.getClockWise(), j).relative(Direction.UP, i));
                    blocksByChunk.computeIfAbsent(new ChunkPos(pos), k -> new ArrayList<>()).add(new wormholePlacementData(pos, pos2, facing, true));
                    blocksByChunk.computeIfAbsent(new ChunkPos(pos2), k -> new ArrayList<>()).add(new wormholePlacementData(pos2, pos, partnerFacing, false));
                }
            }

            for (Map.Entry<ChunkPos, List<wormholePlacementData>> entry : blocksByChunk.entrySet()) {
                ChunkPos chunk =  entry.getKey();
                List<wormholePlacementData> blocks = entry.getValue();

                serverLevel.getChunkSource().addRegionTicket(TicketType.UNKNOWN, chunk, 31, chunk);

                serverLevel.getChunkSource().getChunkFuture(chunk.x, chunk.z, ChunkStatus.FULL, true)
                        .thenAccept(either -> {
                            either.ifSuccess(c -> {

                                for (wormholePlacementData block : blocks) {
                                    BlockPos pos = block.pos;
                                    BlockPos partner = block.partnerPos;
                                    BlockState state = ModBlocks.LINKED_BLOCK.getDefaultState().setValue(FACING, block.facing);
                                    boolean isTankMaster = block.isTankMaster;

                                    //place the block
                                    boolean worked = level.setBlock(pos, state, 3);
                                    //System.out.println("Placed block at " + pos + ": " + worked);

                                    //inject the pos of its partner
                                    if (level.getBlockEntity(pos) instanceof LinkedBlockEntity be) {
                                        be.setLinkedPartner(partner);
                                        be.isTankMaster = isTankMaster;

                                        be.setChanged();
                                        level.sendBlockUpdated(pos, be.getBlockState(), be.getBlockState(), 3);
                                    }
                                }

                                serverLevel.getChunkSource().removeRegionTicket(TicketType.UNKNOWN, chunk, 31, chunk);
                            });
                        });
            }
        });
    }


    private static class wormholePlacementData {
        private BlockPos pos;
        private BlockPos partnerPos;
        private Direction facing;
        private boolean isTankMaster;

        wormholePlacementData(BlockPos pos, BlockPos partnerPos, Direction facing, boolean isTankMaster) {
            this.pos = pos;
            this.partnerPos = partnerPos;
            this.facing = facing;
            this.isTankMaster = isTankMaster;
        }
    }
}
