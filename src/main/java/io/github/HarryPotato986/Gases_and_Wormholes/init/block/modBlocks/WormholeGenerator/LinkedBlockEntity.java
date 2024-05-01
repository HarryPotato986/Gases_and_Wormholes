package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class LinkedBlockEntity extends KineticBlockEntity {

    public @Nullable BlockPos linkedPartner;
    public boolean hasUpdatedSinceLastSync;

    public LinkedBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
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
        super.tick();


    }

    public void syncWithLinkedPartner(Level level) {
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

        if(hasUpdatedSinceLastSync && LBE.hasUpdatedSinceLastSync) {
            //if they are going opposite directions then break the blocks they are facing
            /*otherwise evaluate which block should take on the properties of the other
            and tell that one to propagate the change (use RotationPropagator.handleAdded(level, worldPosition, this))*/
        }
        /*write other cases where one block is updated but the other isn't*/
    }

}
