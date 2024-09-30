package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;
/*
import com.simibubi.create.content.kinetics.RotationPropagator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Objects;

public class Linker {

    public Long LinkID;

    public LinkDataWrapper LBE1;
    public LinkDataWrapper LBE2;


    public Linker(LinkedBlockEntity BE) {
        LBE1 = new LinkDataWrapper(BE);
        LBE2 = new LinkDataWrapper((LinkedBlockEntity) BE.getLevel().getBlockEntity(BE.linkedPartner));
        if(!LinkManager.linkers.containsKey(BE.getLevel())) {
            LinkManager.linkers.put(BE.getLevel(), new HashMap<BlockPos, Linker>());
        }
        LinkManager.linkers.get(BE.getLevel()).put(LBE1.getBlockPos(), this);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {

    }




    public class LinkDataWrapper {
        LinkedBlockEntity blockEntity;
        BlockPos pos;
        @Nullable Long network;
        @Nullable BlockPos source;
        float speed;

        public LinkDataWrapper(LinkedBlockEntity BE) {
            this.blockEntity = BE;
            this.pos = BE.getBlockPos();
            this.network = BE.getOrCreateNetwork().id;
            this.source = BE.source;
            this.speed = BE.getTheoreticalSpeed();
        }

        public void pullUpdate() {
            this.network = blockEntity.getOrCreateNetwork().id;
            this.source = blockEntity.source;
            this.speed = blockEntity.getSpeed();
        }

        public void pushUpdate() {
            float prevSpeed = blockEntity.getSpeed();
            //Long prevNetwork = blockEntity.getOrCreateNetwork().id;
            blockEntity.setNetwork(network, false);
            blockEntity.setSource(source, false);
            blockEntity.setSpeed(speed, false);
            blockEntity.onSpeedChanged(prevSpeed);
            blockEntity.sendData();
            RotationPropagator.handleAdded(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity);
        }

        public LinkedBlockEntity getBlockEntity() {
            return blockEntity;
        }

        public BlockPos getBlockPos() {
            return pos;
        }

        @Nullable
        public Long getNetwork() {
            return network;
        }

        @Nullable
        public BlockPos getSource() {
            return source;
        }

        public float getSpeed() {
            return speed;
        }
    }
}
*/


