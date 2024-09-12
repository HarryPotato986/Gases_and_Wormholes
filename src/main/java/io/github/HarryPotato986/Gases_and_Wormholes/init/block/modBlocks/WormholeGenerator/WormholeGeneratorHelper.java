package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;

public class WormholeGeneratorHelper extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty FIRST_PLACED = BooleanProperty.create("first_placed");

    public WormholeGeneratorHelper(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
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
        boolean isFirstPlaced = state.getValue(FIRST_PLACED);
        if(isFirstPlaced && !level.getBlockTicks().hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);
    }

    @Nullable
    private BlockPos findMasterPos(Level level, BlockPos pos) {
        for(int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    BlockState foundState = level.getBlockState(pos.offset(offset));
                    Block foundBlock = foundState.getBlock();
                    if(foundBlock == BlockInit.WORMHOLE_GENERATOR_CORE.get()) {
                        if(offset.equals(BlockPos.ZERO)) {
                            return pos;
                        }
                        return pos.offset(offset);
                    }
                }
            }
        }
        return null;
    }
}
