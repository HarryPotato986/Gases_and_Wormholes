package io.github.HarryPotato986.Gases_and_Wormholes.networking.packet;

import io.github.HarryPotato986.Gases_and_Wormholes.Gases_and_Wormholes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record WormholeData(BlockPos packetOrigin, BlockPos pos, BlockPos partnerPos, Direction facing, Direction partnerFacing, int size) implements CustomPacketPayload {

    public static final Type<WormholeData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Gases_and_Wormholes.MODID, "wormhole_data"));

    public static final StreamCodec<ByteBuf, WormholeData> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            WormholeData::packetOrigin,

            BlockPos.STREAM_CODEC,
            WormholeData::pos,

            BlockPos.STREAM_CODEC,
            WormholeData::partnerPos,

            Direction.STREAM_CODEC,
            WormholeData::facing,

            Direction.STREAM_CODEC,
            WormholeData::partnerFacing,

            ByteBufCodecs.VAR_INT,
            WormholeData::size,

            WormholeData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
