package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LinkedBlock extends HorizontalKineticBlock implements IBE<LinkedBlockEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty HAS_SHAFT = BooleanProperty.create("has_shaft");
    //public static final BooleanProperty IS_PRIMARY_BLOCK = BooleanProperty.create("is_primary_block");
    //public static final BlockPos PARTNER_POS;

    public static final VoxelShape SHAPE_N = Block.box(0, 0, 6, 16, 16, 16);
    public static final VoxelShape SHAPE_S = Block.box(0, 0, 0, 16, 16, 10);
    public static final VoxelShape SHAPE_E = Block.box(0, 0, 0, 10, 16, 16);
    public static final VoxelShape SHAPE_W = Block.box(6, 0, 0, 10, 16, 16);

    public LinkedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        return switch (facing) {
            case SOUTH -> SHAPE_S;
            case EAST -> SHAPE_E;
            case WEST -> SHAPE_W;
            default -> SHAPE_N;
        };
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);

    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.LINKED_BLOCK_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        /*
        if(world.getBlockEntity(pos.relative(state.getValue(FACING))) instanceof KineticBlockEntity) {
            return face == state.getValue(FACING);
        } else {
            return false;
        }
         */
        return face == state.getValue(FACING);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighborPos) {
        super.onNeighborChange(state, level, pos, neighborPos);

        //if (!level.isClientSide() && neighborPos == pos.relative(state.getValue(FACING)) && level.getBlockEntity(pos) instanceof LinkedBlockEntity be) {
        //    be.onNeighborBlockUpdate();
        //    System.out.println("onNeighborChange");
        //}
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!level.isClientSide() && neighborPos == pos.relative(state.getValue(FACING)) && level.getBlockEntity(pos) instanceof LinkedBlockEntity be) {
            be.onNeighborBlockUpdate();
        }
        //System.out.println("neighborChanged");
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        boolean currentShaftState = state.getValue(HAS_SHAFT);

        // Putting a shaft inside the block
        if (!currentShaftState && heldItem.is(AllBlocks.SHAFT.get().asItem())) {
            if (!level.isClientSide()) {
                // Consume 1 shaft from the player
                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);
                }

                level.setBlock(pos, state.setValue(HAS_SHAFT, true), 3);

                if (level.getBlockEntity(pos) instanceof LinkedBlockEntity be) {
                    BlockPos partnerPos = be.getLinkedPartner();
                    if (level.getBlockState(partnerPos).getBlock() == ModBlocks.LINKED_BLOCK.get() &&
                            level.getBlockEntity(partnerPos) instanceof LinkedBlockEntity) {
                        level.setBlock(partnerPos, state.setValue(HAS_SHAFT, true), 3);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Using the Wrench to remove the shaft
        if (currentShaftState && heldItem.is(AllItems.WRENCH.get())) {
            if (!level.isClientSide()) {
                // Drop the shaft as a physical item in the world
                ItemStack drop = new ItemStack(AllBlocks.SHAFT.get());
                BlockPos dropPos = pos.relative(state.getValue(FACING));
                ItemEntity itemEntity = new ItemEntity(level, dropPos.getX() + 0.5, dropPos.getY() + 0.5, dropPos.getZ() + 0.5, drop);
                level.addFreshEntity(itemEntity);

                level.setBlock(pos, state.setValue(HAS_SHAFT, false), 3);

                if (level.getBlockEntity(pos) instanceof LinkedBlockEntity be) {
                    BlockPos partnerPos = be.getLinkedPartner();
                    if (level.getBlockState(partnerPos).getBlock() == ModBlocks.LINKED_BLOCK.get() &&
                            level.getBlockEntity(partnerPos) instanceof LinkedBlockEntity) {
                        level.setBlock(partnerPos, state.setValue(HAS_SHAFT, false), 3);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public Class<LinkedBlockEntity> getBlockEntityClass() {
        return LinkedBlockEntity.class;
    }

    @Override
    public LinkedBlockEntity getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        return IBE.super.getBlockEntity(worldIn, pos);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LinkedBlockEntity(ModBlockEntities.LINKED_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public BlockEntityType<? extends LinkedBlockEntity> getBlockEntityType() {
        return ModBlockEntities.LINKED_BLOCK_ENTITY.get();
    }

    /*
    @Override
    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity == null ? false : blockentity.triggerEvent(pId, pParam);
    }
    */

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(HAS_SHAFT);
        //pBuilder.add(IS_PRIMARY_BLOCK);
    }
}
