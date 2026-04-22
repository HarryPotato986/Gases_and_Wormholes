package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.foundation.block.IBE;
import io.github.HarryPotato986.Gases_and_Wormholes.init.block.ModBlocks;
import io.github.HarryPotato986.Gases_and_Wormholes.init.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class WormholeGeneratorFluid extends Block implements IBE<WormholeGeneratorFluidEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;


    public WormholeGeneratorFluid(Properties pProperties) {
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
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        removeAll(pLevel, findMasterPos(pLevel, pPos), pPos, pState, pPlayer);
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public void removeAll(Level level, BlockPos masterPos, BlockPos pos, BlockState state, Player player) {
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
                    level.gameEvent(GameEvent.BLOCK_DESTROY, pos.offset(offset), GameEvent.Context.of(player, state));
                    level.destroyBlock(masterPos.offset(offset), false);
                }
            }
        }
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(findMasterPos(pLevel, pPos));
            if(entity instanceof WormholeGeneratorCoreEntity be) {
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(be, Component.literal("Wormhole Generator")), findMasterPos(pLevel, pPos));
            } else {
                throw new IllegalStateException("Our Container provider is missing");
            }
        }

        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public WormholeGeneratorFluidEntity getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        return IBE.super.getBlockEntity(worldIn, pos);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WormholeGeneratorFluidEntity(ModBlockEntities.WORMHOLE_GENERATOR_FLUID_ENTITY.get(), pos, state);
    }

    @Override
    public Class<WormholeGeneratorFluidEntity> getBlockEntityClass() {
        return WormholeGeneratorFluidEntity.class;
    }

    @Override
    public BlockEntityType<? extends WormholeGeneratorFluidEntity> getBlockEntityType() {
        return ModBlockEntities.WORMHOLE_GENERATOR_FLUID_ENTITY.get();
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.WORMHOLE_GENERATOR_FLUID_ENTITY.get(),
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
