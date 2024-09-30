package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.FACING;

public class LinkedBlockEntity extends KineticBlockEntity {

    public BlockPos linkedPartner;
    private int debugPrintDelay = 40;

    public LinkedBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public void setLinkedPartner(BlockPos pos) {
        linkedPartner = pos;
    }

    public BlockPos getLinkedPartner() {
        return linkedPartner;
    }

    public LinkedBlockEntity getLinkedPartnerBE() {
        if (linkedPartner == null) {
            return null;
        }
        BlockEntity otherBE = this.getLevel().getBlockEntity(linkedPartner);
        if (otherBE == null) {
            return null;
        } else if (otherBE instanceof LinkedBlockEntity partnerBE) {
            return partnerBE;
        }
        return null;
    }


    public void tick(Level level, BlockPos pPos, BlockState pState) {
        System.out.println("it is ticking");
        super.tick();
        if (debugPrintDelay <= 0) {
            System.out.println("LinkedBlock at " + getBlockPos() + ":");
            System.out.println("    -Speed: " + getSpeed());
            debugPrintDelay = 40;
        } else {
            debugPrintDelay--;
        }

    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target instanceof LinkedBlockEntity partner && partner.getBlockPos() == this.linkedPartner && !connectedViaAxes) {
            return 1;
        }
        return 0;
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
        return linkedPartner != null && other instanceof LinkedBlockEntity partner && partner.getBlockPos() == linkedPartner;
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


    public Boolean isBEInFrontKinetic() {
        return this.getLevel().getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING)))
                instanceof KineticBlockEntity;
    }
}
