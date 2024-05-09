package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;

public class WormholeGenerator extends HorizontalKineticBlock /*implements IBE<WormholeGeneratorEntity>*/ {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<WormholeGeneratorBlockTypes> BLOCK_FUNCTION = EnumProperty.create("block_function", WormholeGeneratorBlockTypes.class);

    public WormholeGenerator(Properties properties) {
        super(properties);

    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection().getOpposite();

        int setX;
        int toX;
        int setZ;
        int toZ;
        switch (facing) {
            case NORTH -> {setX = -1; toX = 1; setZ = 0; toZ = 2;}
            case SOUTH -> {setX = -1; toX = 1; setZ = -2; toZ = 0;}
            case EAST -> {setX = -2; toX = 0; setZ = -1; toZ = 1;}
            case WEST -> {setX = 0; toX = 2; setZ = -1; toZ = 1;}
            default -> {return null;}
        }



        for(int x = setX; x <= toX; x++) {
            for(int y = 0; y <= 2; y++) {
                for (int z = setZ; z <= toZ; z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    if (offset.equals(BlockPos.ZERO))
                        continue;
                    BlockState occupiedState = context.getLevel()
                            .getBlockState(pos.offset(offset));
                    if (!occupiedState.canBeReplaced())
                        return null;
                }
            }
        }
        return stateForPlacement;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.getBlockTicks()
                .hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        WormholeGeneratorBlockTypes type = pState.getValue(BLOCK_FUNCTION);
        System.out.println(type);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return null;
    }

    /*
    @Override
    public Class<WormholeGeneratorEntity> getBlockEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends WormholeGeneratorEntity> getBlockEntityType() {
        return null;
    }*/

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(BLOCK_FUNCTION);
    }
}

