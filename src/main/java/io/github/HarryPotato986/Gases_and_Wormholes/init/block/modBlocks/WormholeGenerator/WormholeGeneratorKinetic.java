package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class WormholeGeneratorKinetic extends HorizontalKineticBlock implements IBE<WormholeGeneratorKineticEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;


    public WormholeGeneratorKinetic(Properties properties) {
        super(properties);
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
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
    private BlockPos findMasterPos(Level level, BlockPos pos) {
        for(int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    BlockState foundState = level.getBlockState(pos.offset(offset));
                    Block foundBlock = foundState.getBlock();
                    if(foundBlock == ModBlocks.WORMHOLE_GENERATOR_CORE.get()) {
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
            if(entity instanceof WormholeGeneratorCoreEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (WormholeGeneratorCoreEntity) entity, findMasterPos(pLevel, pPos));
            } else {
                throw new IllegalStateException("Our Container provider is missing");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public WormholeGeneratorKineticEntity getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        return IBE.super.getBlockEntity(worldIn, pos);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WormholeGeneratorKineticEntity(ModBlockEntities.WORMHOLE_GENERATOR_KINETIC_ENTITY.get(), pos, state);
    }

    @Override
    public Class<WormholeGeneratorKineticEntity> getBlockEntityClass() {
        return WormholeGeneratorKineticEntity.class;
    }

    @Override
    public BlockEntityType<? extends WormholeGeneratorKineticEntity> getBlockEntityType() {
        return ModBlockEntities.WORMHOLE_GENERATOR_KINETIC_ENTITY.get();
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.WORMHOLE_GENERATOR_KINETIC_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
