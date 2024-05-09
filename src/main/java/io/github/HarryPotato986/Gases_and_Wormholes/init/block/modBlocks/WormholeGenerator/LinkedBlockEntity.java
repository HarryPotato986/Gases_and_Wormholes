package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.FACING;
import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.LinkedBlock.IS_PRIMARY_BLOCK;

public class LinkedBlockEntity extends KineticBlockEntity {

    public @Nullable BlockPos linkedPartner;
    public boolean hasUpdatedSinceLastSync;
    public boolean firstSync = true;
    public boolean spawnPartner = false;
    public boolean isPrimaryBlock;

    public LinkedBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        if(state.getValue(FACING) == Direction.NORTH) {
            spawnPartner = true;
        }
        isPrimaryBlock = state.getValue(IS_PRIMARY_BLOCK);
    }

    public void spawnPartner(Level level, BlockPos pos, BlockState state) {
        BlockPos partnerPos = pos.relative(state.getValue(FACING), 5);
        BlockState BS = BlockInit.LINKED_BLOCK.getDefaultState().setValue(FACING, Direction.EAST).setValue(IS_PRIMARY_BLOCK, false);
        //BlockState newBS = BS.setValue(FACING, Direction.SOUTH);
        level.setBlock(partnerPos, BS, 3);
        LinkedBlockEntity LBE = (LinkedBlockEntity) level.getBlockEntity(partnerPos);
        linkedPartner = partnerPos;
        LBE.linkedPartner = pos;
        spawnPartner = false;
    }

    public void setPartner(BlockPos partnerPos) {
        linkedPartner = partnerPos;
    }

    @Override
    public void attachKinetics() {
        super.attachKinetics();
        hasUpdatedSinceLastSync = true;
    }

    public void tick(Level level, BlockPos pPos, BlockState pState) {
        if(hasUpdatedSinceLastSync) {
            networkDirty = true;
        }

        super.tick();

        if(spawnPartner) {
            spawnPartner(level, pPos, pState);
        }

        if(!firstSync) {
            if(!level.isClientSide() && pPos != null) {
                //syncWithLinkedPartner(level, pPos);
            }
        } else {
            firstSync = false;
        }
        /*
        String cords = getBlockPos().getX() + ", " + getBlockPos().getY() + ", " + getBlockPos().getZ() + ": ";
        String network = this.network + ", ";
        String source = this.source + ", ";
        String speed = this.speed + ", ";
        System.out.println(cords + network + source + speed);
         */
    }

    public void syncWithLinkedPartner(Level level, BlockPos pos) {
        if(linkedPartner == null) {
            return;
        }

        BlockEntity BE = level.getBlockEntity(linkedPartner);
        LinkedBlockEntity LBE;
        if(BE instanceof LinkedBlockEntity) {
            LBE = (LinkedBlockEntity) BE;
        } else {
            return;
        }

        if(hasUpdatedSinceLastSync || LBE.hasUpdatedSinceLastSync) {
            float thisSpeed = getTheoreticalSpeed();
            float otherSpeed = LBE.getTheoreticalSpeed();
            //if they are going opposite directions then break the blocks they are facing
            boolean incompatible = Math.signum(thisSpeed) != Math.signum(otherSpeed) && (thisSpeed != 0 && otherSpeed != 0);
            if(incompatible) {
                destroyFacingBlock(level);
                LBE.destroyFacingBlock(level);
                return;
            } else {
                /*otherwise evaluate which block should take on the properties of the other
                and tell that one to propagate the change (use RotationPropagator.handleAdded(level, worldPosition, this))*/
                if(Math.abs(otherSpeed) > Math.abs(thisSpeed)) {
                    float prevSpeed = getSpeed();
                    setNetwork(LBE.network, false);
                    setSource(LBE.source, false);
                    setSpeed(LBE.getTheoreticalSpeed(), false);
                    onSpeedChanged(prevSpeed);
                    sendData();

                    RotationPropagator.handleAdded(level, pos, this);
                    return;
                }

                if(Math.abs(thisSpeed) > Math.abs(otherSpeed)) {
                    if (hasNetwork() || Objects.equals(network, LBE.network)) {
                        float epsilon = Math.abs(otherSpeed) / 256f / 256f;
                        if (Math.abs(thisSpeed) > Math.abs(otherSpeed) + epsilon) {
                            destroyFacingBlock(level);
                            LBE.destroyFacingBlock(level);
                        }
                        return;
                    }

                    float prevSpeed = LBE.getSpeed();
                    LBE.setNetwork(network, false);
                    LBE.setSource(source, false);
                    LBE.setSpeed(getTheoreticalSpeed(), false);
                    LBE.onSpeedChanged(prevSpeed);
                    LBE.sendData();

                    RotationPropagator.handleAdded(level, linkedPartner, LBE);
                }
            }
            hasUpdatedSinceLastSync = false;
            LBE.hasUpdatedSinceLastSync = false;
        }
    }

    private void destroyFacingBlock(Level level) {
        BlockPos posToDestroy = getBlockPos().relative(getBlockState().getValue(FACING));
        level.destroyBlock(posToDestroy, true);
    }

    public void setNetwork(@org.jetbrains.annotations.Nullable Long networkIn, boolean lastSync) {
        super.setNetwork(networkIn);
        hasUpdatedSinceLastSync = lastSync;
    }

    @Override
    public void setNetwork(@org.jetbrains.annotations.Nullable Long networkIn) {
        super.setNetwork(networkIn);
        hasUpdatedSinceLastSync = true;
    }

    public void setSource(BlockPos source, boolean lastSync) {
        if(source != null) {
            super.setSource(source);
        } else {
            this.source = null;
        }
        hasUpdatedSinceLastSync = lastSync;
    }

    @Override
    public void setSource(BlockPos source) {
        if(source != null) {
            super.setSource(source);
        } else {
            this.source = null;
        }
        hasUpdatedSinceLastSync = true;
    }

    public void setSpeed(float speed, boolean lastSync) {
        super.setSpeed(speed);
        hasUpdatedSinceLastSync = lastSync;
    }

    @Override
    public void setSpeed(float speed) {
        super.setSpeed(speed);
        hasUpdatedSinceLastSync = true;
    }

    public Boolean isBlockEntityInFrontKinetic() {
        return this.getLevel().getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING)))
                instanceof KineticBlockEntity;
    }

    @Nullable
    public BlockEntity getLinkedPartnerBlockEntity() {
        if(linkedPartner == null) {
            return null;
        } else {
            return getLevel().getBlockEntity(linkedPartner);
        }
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if(linkedPartner != null) {
            neighbours.add(linkedPartner);
        }
        return super.addPropagationLocations(block, state, neighbours);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }
}
