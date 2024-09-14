package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.FluidInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.item.ItemInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.WormholeGeneratorMenu;
import io.github.HarryPotato986.Gases_and_Wormholes.util.DirectionWrappedHandler;
import io.github.HarryPotato986.Gases_and_Wormholes.util.InventoryDirectionEntry;
import io.github.HarryPotato986.Gases_and_Wormholes.util.InventoryDirectionWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.WormholeGenerator.WormholeGeneratorKinetic.FACING;

public class WormholeGeneratorKineticEntity extends KineticBlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case LIQUID_NITROGEN_SLOT -> stack.getItem() == ItemInit.LIQUID_NITROGEN_BUCKET.get() || stack.getItem() == Items.BUCKET;
                case BEDROCK_DUST_INPUT -> stack.getItem() == ItemInit.BEDROCK_DUST.get();
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    private static final int LIQUID_NITROGEN_SLOT = 0;
    private static final int BEDROCK_DUST_INPUT = 1;

    private boolean ACQUIRED_FLUID = false;
    private boolean DISTRIBUTED_FLUID = false;

    private final FluidTank LIQUID_NITROGEN_TANK = createFluidTank(10000);


    public String X1 = "";
    public String Y1 = "";
    public String Z1 = "";
    public String X2 = "";
    public String Y2 = "";
    public String Z2 = "";
    public int Wormhole1Facing = 0;
    public int Wormhole2Facing = 0;
    public int WormholeSize = 0;





    private final Map<Direction, LazyOptional<DirectionWrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper(itemHandler,
                    new InventoryDirectionEntry(Direction.DOWN, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.NORTH, BEDROCK_DUST_INPUT, true),
                    new InventoryDirectionEntry(Direction.SOUTH, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.EAST, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.WEST, BEDROCK_DUST_INPUT, false),
                    new InventoryDirectionEntry(Direction.UP, BEDROCK_DUST_INPUT, false)).directionsMap;


    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    private FluidTank createFluidTank(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    blockUpdateAll(level, getBlockPos(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }
        };
    }

    public void blockUpdateAll(Level level, BlockPos pos, BlockState state, int flags) {
        level.sendBlockUpdated(pos, state, state, flags);
        for(int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    BlockState offsetState = level.getBlockState(pos.offset(offset));
                    level.sendBlockUpdated(pos.offset(offset), offsetState, offsetState, flags);
                }
            }
        }
    }

    public WormholeGeneratorKineticEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> WormholeGeneratorKineticEntity.this.progress;
                    case 1 -> WormholeGeneratorKineticEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> WormholeGeneratorKineticEntity.this.progress = pValue;
                    case 1 -> WormholeGeneratorKineticEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }


    public void tick(Level level, BlockPos pPos, BlockState pState) {
        super.tick();
        fillUpOnFluid();
        fillUpOnDust();
    }

    private void fillUpOnDust() {
        if(this.progress < 1) {
            if(!this.itemHandler.getStackInSlot(BEDROCK_DUST_INPUT).isEmpty()) {
                this.itemHandler.extractItem(BEDROCK_DUST_INPUT, 1, false);
                this.progress = this.maxProgress;
            }
        }
    }

    private void fillUpOnFluid() {
        if(this.itemHandler.getStackInSlot(LIQUID_NITROGEN_SLOT).isEmpty()) {
            ACQUIRED_FLUID = false;
            DISTRIBUTED_FLUID = false;
        }else if(hasFluidSourceInSlot(LIQUID_NITROGEN_SLOT)) {
            transferItemFluidToTank(LIQUID_NITROGEN_SLOT, this.LIQUID_NITROGEN_TANK, FluidInit.SOURCE_LIQUID_NITROGEN.get());
        }
    }

    private void transferItemFluidToTank(int fluidInputSlot, FluidTank fluidTank, Fluid fluid) {
        this.itemHandler.getStackInSlot(fluidInputSlot).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
            if(!ACQUIRED_FLUID && this.itemHandler.getStackInSlot(fluidInputSlot).getItem() == Items.BUCKET){
                //int fillAmount = Math.min(fluidTank.getFluidAmount(), 1000);

                int fillAmount = iFluidHandlerItem.fill(fluidTank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                fluidTank.drain(fillAmount, IFluidHandler.FluidAction.EXECUTE);
                this.itemHandler.extractItem(fluidInputSlot, 1, false);
                this.itemHandler.insertItem(fluidInputSlot, iFluidHandlerItem.getContainer(), false);
                DISTRIBUTED_FLUID = true;
            } else if(!DISTRIBUTED_FLUID){
                int drainAmount = Math.min(fluidTank.getSpace(), 1000);

                FluidStack stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if (stack.getFluid() == fluid) {
                    stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                    fillTankWithFluid(stack, iFluidHandlerItem.getContainer(), fluidTank, fluidInputSlot);
                    ACQUIRED_FLUID = true;
                }
            }
        });
    }

    private void fillTankWithFluid(FluidStack stack, ItemStack container, FluidTank fluidTank, int fluidInputSlot) {
        fluidTank.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);

        this.itemHandler.extractItem(fluidInputSlot, 1, false);
        this.itemHandler.insertItem(fluidInputSlot, container, false);
    }

    private boolean hasFluidSourceInSlot(int fluidInputSlot) {
        return this.itemHandler.getStackInSlot(fluidInputSlot).getCount() > 0 &&
                this.itemHandler.getStackInSlot(fluidInputSlot).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }

    public FluidStack getFluid() {
        return LIQUID_NITROGEN_TANK.getFluid();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side != null) {
                Direction localDir = this.getBlockState().getValue(FACING);
                LazyOptional<T> handler = switch (localDir) {
                    case NORTH -> returnCorrectTank(side.getOpposite());
                    case EAST -> returnCorrectTank(side.getClockWise());
                    case SOUTH -> returnCorrectTank(side);
                    case WEST -> returnCorrectTank(side.getCounterClockWise());
                    default -> null;
                };

                if(handler != null) {
                    return handler;
                }
            }
        }

        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(FACING);

                if(side == Direction.DOWN || side == Direction.UP) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(cap);
    }

    private <T> @Nullable LazyOptional<T> returnCorrectTank(@NotNull Direction side) {
        return switch (side) {
            case NORTH -> lazyFluidHandler.cast();
            default -> null;
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> LIQUID_NITROGEN_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.gasesandwormholes.wormhole_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WormholeGeneratorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void write(CompoundTag pTag, boolean clientPacket) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("wormhole_generator.progress", progress);
        pTag.put("OxygenTank", LIQUID_NITROGEN_TANK.writeToNBT(new CompoundTag()));

        pTag.putString("x1", X1);
        pTag.putString("y1", Y1);
        pTag.putString("z1", Z1);
        pTag.putString("x2", X2);
        pTag.putString("y2", Y2);
        pTag.putString("z2", Z2);

        pTag.putInt("wormhole_facing_1", Wormhole1Facing);
        pTag.putInt("wormhole_facing_2", Wormhole2Facing);
        pTag.putInt("wormhole_size", WormholeSize);

        super.write(pTag, clientPacket);
    }

    @Override
    protected void read(CompoundTag pTag, boolean clientPacket) {
        super.read(pTag, clientPacket);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("atmosphere_extractor.progress");
        LIQUID_NITROGEN_TANK.readFromNBT(pTag.getCompound("OxygenTank"));

        X1 = pTag.getString("x1");
        Y1 = pTag.getString("y1");
        Z1 = pTag.getString("z1");
        X2 = pTag.getString("x2");
        Y2 = pTag.getString("y2");
        Z2 = pTag.getString("z2");

        Wormhole1Facing = pTag.getInt("wormhole_facing_1");
        Wormhole2Facing = pTag.getInt("wormhole_facing_2");
        WormholeSize = pTag.getInt("wormhole_size");
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(connection, packet);
    }

    public void updateScreenData(String x1, String y1, String z1, String x2, String y2, String z2, int facing1Index, int facing2Index, int sizeIndex) {
        System.out.println("yerp");
        System.out.println(x1);
        X1 = x1;
        Y1 = y1;
        Z1 = z1;
        X2 = x2;
        Y2 = y2;
        Z2 = z2;
        Wormhole1Facing = facing1Index;
        Wormhole2Facing = facing2Index;
        WormholeSize = sizeIndex;
    }

    public String[] getEditBoxData() {
        return new String[]{X1,Y1,Z1,X2,Y2,Z2};
    }

    public int[] getButtonData() {
        return new int[]{Wormhole1Facing, Wormhole2Facing, WormholeSize};
    }

}
