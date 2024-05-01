package io.github.HarryPotato986.Gases_and_Wormholes.init.block.modBlocks.AtmosphereExtractor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.HarryPotato986.Gases_and_Wormholes.init.fluid.FluidInit;
import io.github.HarryPotato986.Gases_and_Wormholes.init.recipe.AtmosphereExtractorRecipe;
import io.github.HarryPotato986.Gases_and_Wormholes.init.screen.AtmosphereExtractorMenu;
import io.github.HarryPotato986.Gases_and_Wormholes.util.*;
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
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AtmosphereExtractorEntity extends KineticBlockEntity implements MenuProvider {
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
                case NITROGEN_SLOT, OXYGEN_SLOT -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                //case OUTPUT_SLOT -> false;
                //case ENERGY_ITEM_SLOT -> stack.getItem() == ItemInit.BEDROCK_DUST.get();
                default -> super.isItemValid(slot, stack);
            };
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };



    private static final int OXYGEN_SLOT = 0;
    private static final int NITROGEN_SLOT = 1;
    //private static final int OUTPUT_SLOT = 2;
    //private static final int ENERGY_ITEM_SLOT = 3;

    private boolean[] ACQUIRED_FLUID = new boolean[]{false,false};
    private boolean[] DISTRIBUTED_FLUID = new boolean[]{false,false};

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyNitrogenHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyOxygenHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<DirectionWrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper(itemHandler,
                    new InventoryDirectionEntry(Direction.DOWN, OXYGEN_SLOT, false),
                    new InventoryDirectionEntry(Direction.NORTH, NITROGEN_SLOT, false),
                    new InventoryDirectionEntry(Direction.SOUTH, OXYGEN_SLOT, false),
                    new InventoryDirectionEntry(Direction.EAST, NITROGEN_SLOT, true),
                    new InventoryDirectionEntry(Direction.WEST, OXYGEN_SLOT, true),
                    new InventoryDirectionEntry(Direction.UP, OXYGEN_SLOT, false)).directionsMap;

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    private final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();
    private final FluidTank NITROGEN_TANK = createFluidTank(2000);
    private final FluidTank OXYGEN_TANK = createFluidTank(2000);


    private FluidTank createFluidTank(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }
        };
    }

    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(64000, 200) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };

    }


    public AtmosphereExtractorEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState) {
        super(type, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AtmosphereExtractorEntity.this.progress;
                    case 1 -> AtmosphereExtractorEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AtmosphereExtractorEntity.this.progress = pValue;
                    case 1 -> AtmosphereExtractorEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }

    public FluidStack getFluid(int index) {
        return switch (index) {
            case 0 -> NITROGEN_TANK.getFluid();
            case 1 -> OXYGEN_TANK.getFluid();
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER) {
            Direction localDir = this.getBlockState().getValue(AtmosphereExtractor.FACING);
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

        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(AtmosphereExtractor.FACING);

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

        return super.getCapability(cap, side);
    }

    private <T> @Nullable LazyOptional<T> returnCorrectTank(@NotNull Direction side) {
        return switch (side) {
            case WEST -> lazyNitrogenHandler.cast();
            case EAST -> lazyOxygenHandler.cast();
            default -> null;
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyNitrogenHandler = LazyOptional.of(() -> NITROGEN_TANK);
        lazyOxygenHandler = LazyOptional.of(() -> OXYGEN_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyNitrogenHandler.invalidate();
        lazyOxygenHandler.invalidate();
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
        return Component.translatable("block.gasesandwormholes.atmosphere_extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AtmosphereExtractorMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void write(CompoundTag pTag, boolean clientPacket) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("atmosphere_extractor.progress", progress);
        pTag.putInt("energy", ENERGY_STORAGE.getEnergyStored());
        pTag.put("NitrogenTank", NITROGEN_TANK.writeToNBT(new CompoundTag()));
        pTag.put("OxygenTank", OXYGEN_TANK.writeToNBT(new CompoundTag()));
        //pTag = NITROGEN_TANK.writeToNBT(pTag);
        //pTag = OXYGEN_TANK.writeToNBT(pTag);

        super.write(pTag, clientPacket);
    }

    @Override
    protected void read(CompoundTag pTag, boolean clientPacket) {
        super.read(pTag, clientPacket);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("atmosphere_extractor.progress");
        ENERGY_STORAGE.setEnergy(pTag.getInt("energy"));
        NITROGEN_TANK.readFromNBT(pTag.getCompound("NitrogenTank"));
        OXYGEN_TANK.readFromNBT(pTag.getCompound("OxygenTank"));
    }

    public void tick(Level level, BlockPos pPos, BlockState pState) {
        super.tick();
        fillUpOnFluid();

        updateMaxProgress();

        if(checkIfTankHadSpace(NITROGEN_TANK,78) && checkIfTankHadSpace(OXYGEN_TANK,21)) {
            progress++;
            setChanged(level, pPos, pState);

            if(progress >= maxProgress) {
                generateFluid(NITROGEN_TANK, FluidInit.NITROGEN_GAS.get(),78);
                generateFluid(OXYGEN_TANK, FluidInit.OXYGEN_GAS.get(),21);
                progress = 0;
            }
        }

        /*
        fillUpOnEnergy();
        fillUpOnFluid();

        if(isOutputSlotEmptyOrReceivable() && hasRecipe()) {
            increaseCraftingProgress();
            extractEnergy();
            setChanged(level, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                extractFluid();
                resetProgress();
            }
        }else{
            resetProgress();
        }
        */
    }

    private void generateFluid(FluidTank tank, Fluid fluid, int quantity) {
        tank.fill(new FluidStack(fluid, quantity), IFluidHandler.FluidAction.EXECUTE);
    }

    private boolean checkIfTankHadSpace(FluidTank tank, int inputQuantity) {
        return tank.getFluidAmount() + inputQuantity <= tank.getCapacity();
    }

    private void updateMaxProgress() {
        float newProductionSpeed = Math.max(((3 * 128) / Math.abs(getSpeed())), 1.0f);
        maxProgress = Math.round(newProductionSpeed * 20.0f);
    }


    private void fillUpOnFluid() {
        if(this.itemHandler.getStackInSlot(OXYGEN_SLOT).isEmpty()) {
            ACQUIRED_FLUID[OXYGEN_SLOT] = false;
            DISTRIBUTED_FLUID[OXYGEN_SLOT] = false;
        }else if(hasFluidSourceInSlot(OXYGEN_SLOT)) {
            transferItemFluidToTank(OXYGEN_SLOT, this.OXYGEN_TANK, FluidInit.OXYGEN_GAS.get());
        }

        if(this.itemHandler.getStackInSlot(NITROGEN_SLOT).isEmpty()) {
            ACQUIRED_FLUID[NITROGEN_SLOT] = false;
            DISTRIBUTED_FLUID[NITROGEN_SLOT] = false;
        }else if(hasFluidSourceInSlot(NITROGEN_SLOT)) {
            transferItemFluidToTank(NITROGEN_SLOT, this.NITROGEN_TANK, FluidInit.NITROGEN_GAS.get());
        }
    }

    private void transferItemFluidToTank(int fluidInputSlot, FluidTank fluidTank, Fluid fluid) {
        this.itemHandler.getStackInSlot(fluidInputSlot).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(iFluidHandlerItem -> {
            if(!ACQUIRED_FLUID[fluidInputSlot] && this.itemHandler.getStackInSlot(fluidInputSlot).getItem() == Items.BUCKET){
                //int fillAmount = Math.min(fluidTank.getFluidAmount(), 1000);

                int fillAmount = iFluidHandlerItem.fill(fluidTank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
                fluidTank.drain(fillAmount, IFluidHandler.FluidAction.EXECUTE);
                this.itemHandler.extractItem(fluidInputSlot, 1, false);
                this.itemHandler.insertItem(fluidInputSlot, iFluidHandlerItem.getContainer(), false);
                DISTRIBUTED_FLUID[fluidInputSlot] = true;
            } else if(!DISTRIBUTED_FLUID[fluidInputSlot]){
                int drainAmount = Math.min(fluidTank.getSpace(), 1000);

                FluidStack stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if (stack.getFluid() == fluid) {
                    stack = iFluidHandlerItem.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                    fillTankWithFluid(stack, iFluidHandlerItem.getContainer(), fluidTank, fluidInputSlot);
                    ACQUIRED_FLUID[fluidInputSlot] = true;
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



    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(connection, packet);
    }






    /*
    private void extractFluid() {
        this.NITROGEN_TANK.drain(500, IFluidHandler.FluidAction.EXECUTE);
    }

    private void extractEnergy() {
        this.ENERGY_STORAGE.extractEnergy(100, false);
    }

    private void fillUpOnEnergy() {
        if(hasEnergyItemInSlot(ENERGY_ITEM_SLOT)) {
            this.ENERGY_STORAGE.receiveEnergy(3200, false);
        }
    }

    private boolean hasEnergyItemInSlot(int energyItemSlot) {
        return !this.itemHandler.getStackInSlot(energyItemSlot).isEmpty() &&
                this.itemHandler.getStackInSlot(energyItemSlot).getItem() == ItemInit.BEDROCK_DUST.get();
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<AtmosphereExtractorRecipe> recipe = getCurrentRecipe();
        ItemStack resultItem = recipe.get().getResultItem(getLevel().registryAccess());

        this.itemHandler.extractItem(OXYGEN_SLOT,1,false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(resultItem.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + resultItem.getCount()));
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<AtmosphereExtractorRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack resultItem = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(resultItem.getCount())
                && canInsertItemIntoOutputSlot(resultItem.getItem()) && hasEnoughEnergyToCraft()
                && hasEnoughFluidToCraft();
    }

    private boolean hasEnoughFluidToCraft() {
        return this.NITROGEN_TANK.getFluidAmount() >= 500;
    }

    private boolean hasEnoughEnergyToCraft() {
        return this.ENERGY_STORAGE.getEnergyStored() >= 100 * maxProgress;
    }

    private Optional<AtmosphereExtractorRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(AtmosphereExtractorRecipe.Type.INSTANCE, inventory, level);
    }


    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

     */
}


