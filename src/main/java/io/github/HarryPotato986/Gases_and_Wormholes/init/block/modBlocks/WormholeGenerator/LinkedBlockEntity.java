package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor.AtmosphereExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.*;

public class LinkedBlockEntity extends KineticBlockEntity {

    public BlockPos linkedPartner;
    public boolean isTankMaster = false;
    public boolean isSyncedTankDirty;
    private int debugPrintDelay = 40;

    public LinkedBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    /*
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new LinkedBlockFluidTransportBehaviour(this));
        super.addBehaviours(behaviours);
    }
    */

    public void setLinkedPartner(BlockPos pos) {
        linkedPartner = pos;
    }

    public BlockPos getLinkedPartner() {
        return linkedPartner;
    }

    public LinkedBlockEntity getPartnerBE() {
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


    //Fluid Stuff
    public void dirtySyncedTank() {
        this.isSyncedTankDirty = true;
    }

    private final FluidTank TANK = createFluidTank(1000);

    private FluidTank createFluidTank(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }
        };
    }

    public IFluidHandler getFluidHandler(Direction side) {
        if (!(getBlockState().getValue(TYPE) == LinkedBlockTypes.PIPE)) {
            return null;
        }

        if(side == null) {
            return TANK;
        }
        Direction facing = this.getBlockState().getValue(LinkedBlock.FACING);
        if (side == facing) {
            if (isTankMaster) {
                return this.TANK;
            }
            if (level != null && linkedPartner != null && level.isLoaded(linkedPartner)
                    && level.getBlockEntity(linkedPartner) instanceof LinkedBlockEntity be) {
                return be.getFluidHandler(null);
            }
            dirtySyncedTank();
            return TANK;
        }
        return null;
    }


    public void tick(Level level, BlockPos pPos, BlockState pState) {
        //System.out.println("it is ticking");
        super.tick();

        if (debugPrintDelay <= 0) {
            System.out.println("LinkedBlock at " + getBlockPos() + ":");
            System.out.println("    -isMaster: " + isTankMaster);
            System.out.println("    -Speed: " + getSpeed());
            System.out.println("    -Partner at: " + linkedPartner);
            System.out.println("    -Network: " + network);

            debugPrintDelay = 60;
        } else {
            debugPrintDelay--;
        }


        if (!isTankMaster) {
            if (isSyncedTankDirty && level.isLoaded(linkedPartner) && level.getBlockEntity(linkedPartner) instanceof LinkedBlockEntity partner) {
                partner.dirtySyncedTank();
            }
        } else {
            if (isSyncedTankDirty && level.isLoaded(linkedPartner) && level.getBlockEntity(linkedPartner) instanceof LinkedBlockEntity partner) {
                IFluidHandler masterTank = TANK;
                IFluidHandler partnerTank = partner.getFluidHandler(null);

                if (masterTank.getFluidInTank(0).getFluid() != partnerTank.getFluidInTank(0).getFluid()) {
                    masterTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                }

                int drained = masterTank.fill(partnerTank.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);
                partnerTank.drain(drained, IFluidHandler.FluidAction.EXECUTE);

                partner.isSyncedTankDirty = false;
                if (partnerTank.getFluidInTank(0).isEmpty()) {
                    this.isSyncedTankDirty = false;
                }
            }
        }
    }

    //Custom Kinetic Propagation
    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {

        boolean hasShaft = stateFrom.getValue(TYPE) == LinkedBlockTypes.SHAFT && stateTo.getValue(TYPE) == LinkedBlockTypes.SHAFT;

        if (target instanceof LinkedBlockEntity && target.getBlockPos().equals(this.linkedPartner) && !connectedViaAxes && hasShaft) {
            return switch (stateFrom.getValue(FACING)) {
                case NORTH, WEST -> stateTo.getValue(FACING).equals(Direction.WEST) || stateTo.getValue(FACING).equals(Direction.NORTH) ? -1 : 1;
                case SOUTH, EAST -> stateTo.getValue(FACING).equals(Direction.WEST) || stateTo.getValue(FACING).equals(Direction.NORTH) ? 1 : -1;
                default -> 0;
            };
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
        pTag.putBoolean("isTankMaster", isTankMaster);
        pTag.putBoolean("isSyncedTankDirty", isSyncedTankDirty);

        super.write(pTag, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag pTag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(pTag, registries, clientPacket);

        linkedPartner = NbtUtils.readBlockPos(pTag, "linked_partner").orElse(null);
        isTankMaster = pTag.getBoolean("isTankMaster");
        isSyncedTankDirty = pTag.getBoolean("isSyncedTankDirty");
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


    /* From when I tried to connect to the Create Fluid network
    class LinkedBlockFluidTransportBehaviour extends FluidTransportBehaviour {

        public LinkedBlockFluidTransportBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public boolean canHaveFlowToward(BlockState state, Direction direction) {
            return LinkedBlock.isLinkedBlock(state) &&
                    (direction == getBlockState().getValue(FACING) || direction == Direction.UP);
        }
    }
     */
}
