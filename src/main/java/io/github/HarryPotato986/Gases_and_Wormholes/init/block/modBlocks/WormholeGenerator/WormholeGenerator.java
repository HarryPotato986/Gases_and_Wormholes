package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.BlockInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.TileEntitiesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class WormholeGenerator extends HorizontalKineticBlock implements IBE<WormholeGeneratorEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<WormholeGeneratorBlockTypes> BLOCK_FUNCTION = EnumProperty.create("block_function", WormholeGeneratorBlockTypes.class);
    public static final BooleanProperty FIRST_PLACED = BooleanProperty.create("first_placed");


    public WormholeGenerator(Properties properties) {
        super(properties);

    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        if(pState.getValue(BLOCK_FUNCTION) != WormholeGeneratorBlockTypes.CORE) {
            return RenderShape.INVISIBLE;
        }
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        if(state.getValue(BLOCK_FUNCTION) == WormholeGeneratorBlockTypes.KINETIC_INPUT) {
            return face == state.getValue(FACING);
        }
        return false;
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
                    if(foundBlock == BlockInit.WORMHOLE_GENERATOR.get()) {
                        if(foundState.getValue(BLOCK_FUNCTION) == WormholeGeneratorBlockTypes.CORE) {
                            if(offset.equals(BlockPos.ZERO)) {
                                return pos;
                            }
                            return pos.offset(offset);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction facing = pState.getValue(FACING);

        int setX;
        int toX;
        int setZ;
        int toZ;
        BlockPos kineticOffset;
        BlockPos fluidOffset;
        BlockPos itemOffset;
        BlockPos coreOffset;
        switch (facing) {
            case NORTH -> {
                setX = -1; toX = 1; setZ = 0; toZ = 2;
                kineticOffset = new BlockPos(0, 1, 0);
                fluidOffset = new BlockPos(1, 1, 1);
                itemOffset = new BlockPos(-1, 1, 1);
                coreOffset = new BlockPos(0, 1, 1);
            }
            case SOUTH -> {
                setX = -1; toX = 1; setZ = -2; toZ = 0;
                kineticOffset = new BlockPos(0, 1, 0);
                fluidOffset = new BlockPos(-1, 1, -1);
                itemOffset = new BlockPos(1, 1, -1);
                coreOffset = new BlockPos(0, 1, -1);
            }
            case EAST -> {
                setX = -2; toX = 0; setZ = -1; toZ = 1;
                kineticOffset = new BlockPos(0, 1, 0);
                fluidOffset = new BlockPos(-1, 1, 1);
                itemOffset = new BlockPos(-1, 1, -1);
                coreOffset = new BlockPos(-1, 1, 0);
            }
            case WEST -> {
                setX = 0; toX = 2; setZ = -1; toZ = 1;
                kineticOffset = new BlockPos(0, 1, 0);
                fluidOffset = new BlockPos(1, 1, -1);
                itemOffset = new BlockPos(1, 1, 1);
                coreOffset = new BlockPos(1, 1, 0);
            }
            default -> {return;}
        }

        for(int x = setX; x <= toX; x++) {
            for(int y = 0; y <= 2; y++) {
                for (int z = setZ; z <= toZ; z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    if (offset.equals(BlockPos.ZERO))
                        continue;

                    BlockState blockState = BlockInit.WORMHOLE_GENERATOR.getDefaultState()
                            .setValue(FIRST_PLACED, false)
                            .setValue(FACING, facing);
                    if(offset.equals(kineticOffset)) {
                        BlockState kineticBlockState = blockState.setValue(BLOCK_FUNCTION, WormholeGeneratorBlockTypes.KINETIC_INPUT);
                        pLevel.setBlockAndUpdate(pPos.offset(kineticOffset), kineticBlockState);
                    } else if(offset.equals(fluidOffset)) {
                        BlockState fluidBlockState = blockState.setValue(FACING, facing.getClockWise())
                                .setValue(BLOCK_FUNCTION, WormholeGeneratorBlockTypes.FLUID_INPUT);
                        pLevel.setBlockAndUpdate(pPos.offset(fluidOffset), fluidBlockState);
                    } else if(offset.equals(itemOffset)) {
                        BlockState itemBlockState = blockState.setValue(FACING, facing.getCounterClockWise())
                                .setValue(BLOCK_FUNCTION, WormholeGeneratorBlockTypes.ITEM_INPUT);
                        pLevel.setBlockAndUpdate(pPos.offset(itemOffset), itemBlockState);
                    } else if(offset.equals(coreOffset)) {
                        BlockState coreBlockState = blockState.setValue(BLOCK_FUNCTION, WormholeGeneratorBlockTypes.CORE);
                        pLevel.setBlockAndUpdate(pPos.offset(coreOffset), coreBlockState);
                    } else {
                        pLevel.setBlockAndUpdate(pPos.offset(offset), blockState);
                    }


                }
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof WormholeGeneratorEntity) {
                ((WormholeGeneratorEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        removeAll(pLevel, findMasterPos(pLevel, pPos), pPos);
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public void removeAll(Level level, BlockPos masterPos, BlockPos pos) {
        if(masterPos == null) {
            masterPos = findMasterPos(level, pos);
        }

        for(int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    if(masterPos.offset(offset) == pos) {
                        continue;
                    }
                    level.destroyBlock(masterPos.offset(offset), false);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(findMasterPos(pLevel, pPos));
            if(entity instanceof WormholeGeneratorEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (WormholeGeneratorEntity) entity, findMasterPos(pLevel, pPos));
            } else {
                throw new IllegalStateException("Our Container provider is missing");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    private BlockState getMasterState(Level level, BlockPos pos) {
        return level.getBlockState(findMasterPos(level, pos));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return null;
    }

    @Nullable
    @Override
    public WormholeGeneratorEntity getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        return IBE.super.getBlockEntity(worldIn, findMasterPos(worldIn.getBlockEntity(pos).getLevel(), pos));
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        WormholeGeneratorBlockTypes function = state.getValue(BLOCK_FUNCTION);
        if(function == WormholeGeneratorBlockTypes.CORE) {
            return new WormholeGeneratorEntity(TileEntitiesInit.WORMHOLE_GENERATOR_ENTITY.get(), pos, state);
        } else {
            return null;
        }

    }


    @Override
    public Class<WormholeGeneratorEntity> getBlockEntityClass() {
        return WormholeGeneratorEntity.class;
    }

    @Override
    public BlockEntityType<? extends WormholeGeneratorEntity> getBlockEntityType() {
        return TileEntitiesInit.WORMHOLE_GENERATOR_ENTITY.get();
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, TileEntitiesInit.WORMHOLE_GENERATOR_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, findMasterPos(pLevel1, pPos), pState1));
    }

    @org.jetbrains.annotations.Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(BLOCK_FUNCTION);
        pBuilder.add(FIRST_PLACED);
    }
}

