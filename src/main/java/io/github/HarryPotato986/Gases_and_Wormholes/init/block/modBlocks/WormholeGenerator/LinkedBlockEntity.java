package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.FACING;
import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.HAS_SHAFT;

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
        //System.out.println("it is ticking");
        super.tick();
        if (debugPrintDelay <= 0) {
            System.out.println("LinkedBlock at " + getBlockPos() + ":");
            System.out.println("    -Speed: " + getSpeed());
            System.out.println("    -Partner at: " + linkedPartner);
            System.out.println("    -Network: " + network);

            debugPrintDelay = 60;
        } else {
            debugPrintDelay--;
        }
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target instanceof LinkedBlockEntity && target.getBlockPos().equals(this.linkedPartner) && !connectedViaAxes && stateFrom.getValue(HAS_SHAFT) && stateTo.getValue(HAS_SHAFT)) {
            return -1;
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
    protected void write(CompoundTag pTag, HolderLookup.Provider registries, boolean clientPacket) {
        if (this.linkedPartner != null) {
            pTag.put("linked_partner", NbtUtils.writeBlockPos(this.linkedPartner));
        }

        super.write(pTag, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag pTag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(pTag, registries, clientPacket);

        linkedPartner = NbtUtils.readBlockPos(pTag, "linked_partner").orElse(null);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.level != null && !this.level.isClientSide()) {
            this.detachKinetics();
            this.removeSource();

            //com.simibubi.create.content.kinetics.RotationPropagator.handleRemoved(this.level, this.worldPosition, this);
            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());

            RotationPropagator.handleRemoved(this.level, this.worldPosition, this);
            RotationPropagator.handleAdded(this.level, this.worldPosition, this);

            this.notifyUpdate();
        }
    }

    public Boolean isBlockInFrontKinetic() {
        return this.getLevel().getBlockState(getBlockPos().relative(getBlockState().getValue(FACING))).getBlock()
                instanceof KineticBlock;
    }

    public Boolean shouldRenderShaft() {
        if (isBlockInFrontKinetic()) {return true;}
        if (this.linkedPartner != null && (this.getLevel().getBlockEntity(this.linkedPartner) instanceof LinkedBlockEntity partner)) {
            return partner.isBlockInFrontKinetic();
        }
        return false;
    }

    public void onNeighborBlockUpdate() {
        this.setChanged();

        if (level != null) {
            level.sendBlockUpdated(worldPosition,  this.getBlockState(), this.getBlockState(), 3);
        }


    }
}
