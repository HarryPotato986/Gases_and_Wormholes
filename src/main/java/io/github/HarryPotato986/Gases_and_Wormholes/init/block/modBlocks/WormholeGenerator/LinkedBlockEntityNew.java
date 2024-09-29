package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class LinkedBlockEntityNew extends KineticBlockEntity {

    public BlockPos linkedPartner;

    public LinkedBlockEntityNew(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public void setLinkedPartner(BlockPos pos) {
        linkedPartner = pos;
    }


    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target instanceof LinkedBlockEntity partner && partner.getBlockPos() == this.linkedPartner && !connectedViaAxes) {
            return 1f;
        }
        return 0f;
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (linkedPartner != null) {
            neighbours.add(linkedPartner);
        }
        return neighbours;
    }

    @Override
    public boolean isCustomConnection(KineticBlockEntity other, BlockState state, BlockState otherState) {
        return other instanceof LinkedBlockEntity;
    }

    @Override
    protected void write(CompoundTag pTag, boolean clientPacket) {
        pTag.put("linked_partner", NbtUtils.writeBlockPos(this.linkedPartner));

        super.write(pTag, clientPacket);
    }

    @Override
    protected void read(CompoundTag pTag, boolean clientPacket) {
        linkedPartner = NbtUtils.readBlockPos(pTag.getCompound("linked_partner"));

        super.read(pTag, clientPacket);
    }
}
